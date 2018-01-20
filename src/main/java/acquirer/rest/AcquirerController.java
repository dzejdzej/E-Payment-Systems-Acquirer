package acquirer.rest;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import acquirer.bean.Merchant;
import acquirer.bean.StanjeTransakcije;
import acquirer.bean.Transakcija;
import acquirer.repository.MerchantRepository;
import acquirer.repository.TransakcijaRepository;
import acquirer.websocket.AcquirerWebSocketController;



@RestController
@CrossOrigin
@RequestMapping("/acquirerMain")
public class AcquirerController {

	@Autowired
	private MerchantRepository merchantRepository;

	@Autowired
	private TransakcijaRepository transakcijaRepository;
	
	@Autowired
	AcquirerWebSocketController acquirerWebsocket;

	@Value("${acquirer.web.app.url}")
	private String acquirerWebAppUrl;
	
	@Value("${pcc.url}")
	private String pccUrl; 
	
	@Value("${insurance.web.app.failed.url}")
	private String failedUrl; 
	
	@Value("${pc.url}")
	private String pcUrl;
	
	@Value("${error.origin.name}")
	private String errorOriginName; 
	
	private RestTemplate rt = new RestTemplate();

	private final Log logger = LogFactory.getLog(this.getClass());
	
	@RequestMapping(value = "/requestPayment", method = RequestMethod.POST)
	public ResponseEntity<?> requestPayment(@RequestBody PCNewPaymentDTO pcNewPaymentDTO) {

//		Map<String, Merchant> knownMerchants = new HashMap<>();
//		List<Merchant> merchants = merchantRepository.findAll();
//		for (Merchant merchant : merchants) {
//			knownMerchants.put(merchant.getIdMerchant(), merchant);
//		}
		RequestPaymentAquirerDTO dto = new RequestPaymentAquirerDTO();
		
		Merchant merchant = merchantRepository.findByIdMerchant(pcNewPaymentDTO.getMerchantId()); 

		if (merchant == null || !merchant.getPasswordMerchant().equals(pcNewPaymentDTO.getMerchantPassword())) {
			dto.setPaymentURL(failedUrl.replace("{id}","" + pcNewPaymentDTO.getMerchantOrderId()));
			dto.setPaymentID(-1);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(dto);
		}

		Transakcija transakcija = new Transakcija();
		transakcija.setTransactionIdMerchant(pcNewPaymentDTO.getMerchantOrderId());
		transakcija.setStanje(StanjeTransakcije.Zapoceta);
		transakcija.setAmount(pcNewPaymentDTO.getAmmount());
		System.out.println("Amount:           " + pcNewPaymentDTO.getAmmount());

		transakcija.setVreme(new Date());
		transakcija.setMerchant(merchant);
		transakcija.setErrorUrl(pcNewPaymentDTO.getErrorURL());
		transakcija.setFailedUrl(pcNewPaymentDTO.getFailedURL());
		transakcija.setSuccessUrl(pcNewPaymentDTO.getSuccessURL());
		transakcija = transakcijaRepository.save(transakcija);

		
		dto.setPaymentID(transakcija.getId());
		dto.setPaymentURL(acquirerWebAppUrl.replace("{id}", "" + transakcija.getId()));
		//dto.setPaymentURL(acquirerWebAppUrl);
		
		return ResponseEntity.ok(dto);
	}

	@RequestMapping(value = "/getPaymentInfo/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getPaymentInfo(@PathVariable(value = "id") int id) {
		Transakcija transakcija = transakcijaRepository.findById(id);
		System.out.println("TRANSAKCIJA    " + transakcija);
		TransactionDTO dto = new TransactionDTO(transakcija);
		if (dto == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(dto);
	}

	@RequestMapping(value = "/completePayment", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void completePayment(@RequestBody CompletePaymentDTO completePaymentDTO) {
	
		Transakcija transakcija = transakcijaRepository.findById(completePaymentDTO.getAcquirerOrderId()); 
		Merchant merchant = merchantRepository.findByIdMerchant(completePaymentDTO.getIdMerchant()); 
		transakcija.setMerchant(merchant);
		transakcija.setStanje(StanjeTransakcije.Zapoceta);
		transakcija.setTransactionIdMerchant(completePaymentDTO.getTransactionIdMerchant());
		transakcija.setVreme(new Date());
		transakcija = transakcijaRepository.save(transakcija); 
		
		completePaymentDTO.setAcquirerOrderId(transakcija.getId());
		completePaymentDTO.setAcquirerTimestamp(new Date());
		completePaymentDTO.setAmount(transakcija.getAmount());
		
		
		String url = "http://" + this.pccUrl + "/pccMain/completePaymentRequest";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<CompletePaymentDTO> completePaymentRequest = new HttpEntity<>(completePaymentDTO);
		CompletePaymentResponseDTO compResponse = new CompletePaymentResponseDTO(); 
		
		
		try {
			ResponseEntity<CompletePaymentResponseDTO> completePaymentResponse = rt.postForEntity(url, completePaymentRequest,
					CompletePaymentResponseDTO.class);
			 compResponse = completePaymentResponse.getBody();
		}catch(HttpClientErrorException e) {
			logger.error(e.getMessage(), e.getCause());
			logger.error("PAYMENT_DTO");
			if(e.getStatusCode() == HttpStatus.CONFLICT) {
				// failed url 
				// failed due to...
				logger.error("Payment #" + transakcija.getId() + " failed due to:" + e.getResponseBodyAsString());
				compResponse = CompletePaymentResponseDTO.parse(e.getResponseBodyAsString());
			}
			
			
		}catch(Exception e) {
			logger.error(e.getMessage(), e.getCause());
			logger.error("PAYMENT_DTO");
			compResponse = new CompletePaymentResponseDTO();
			compResponse.setErrorInfo(e.getMessage() + "" + e.getCause());
		}
		
		
		transakcija = transakcijaRepository.findById(completePaymentDTO.getAcquirerOrderId());
		compResponse.setErrorURL(transakcija.getErrorUrl());
		compResponse.setFailedURL(transakcija.getFailedUrl());
		compResponse.setSuccessURL(transakcija.getSuccessUrl());
		if(!compResponse.getSuccess()) {
			transakcija.setStanje(StanjeTransakcije.Ponistena);
			transakcija.setRazlog(compResponse.getReason());
		}
		else {
			transakcija.setStanje(StanjeTransakcije.Proknjizena);
		}
		
		transakcijaRepository.save(transakcija); 
		
		/////////////////////////////////////
		/// SALJI DALJE NAZAD DA SE PROKNJIZI
		/////////////////////////////////////
		
		ResponseDTO responseDTO = new ResponseDTO(); 
		responseDTO.setErrorInfo(compResponse.getErrorInfo());
		responseDTO.setReason(compResponse.getReason());
		responseDTO.setSuccess(compResponse.getSuccess());
		responseDTO.setTransactionIdMerchant(transakcija.getTransactionIdMerchant());
		
		url = "http://" + this.pcUrl + "/paymentConcentratorMain/completePaymentResponse";

		HttpEntity<ResponseDTO> completePaymentResponse1= new HttpEntity<>(responseDTO);
		
		try {
			rt.postForEntity(url, completePaymentResponse1, String.class);
		}catch(Exception e) {
			logger.error("Failed to send transcation results to DMZ, details:" + e.getMessage());
			logger.error(e.getCause());
			e.printStackTrace();
			compResponse.setReason("COMMUNICATION ERROR");
		}
		
		acquirerWebsocket.sendPaymentResponse(compResponse);
		
		//return new ResponseEntity(HttpStatus.OK);
		
		//naci transakciju po id ako je prosla i staviti je da je prosla, ako nije staviti da je fajlovala
		// i razlog zasto je fejlovala 
		//preko rest template gadjaj onog ko je tebe gadjao ovde je to pc
		//proslediti mu citav dto
		//nazad vracaj morderid, aorderid, atimestamp, paymentid to je mozda issuer order id to je onaj sa pocetka id tr u aquireru
		//prosledi sve do dmz i svuda u bazu upisi u data centru da bismo znali da je polisa placena
		//success url postoji sansa da se okine pre nego sto se upise u bazama
		//to znaci da u angularu treba par puta probati da se getuje po id info o placanju polise pre nego sto se
		//odustane 
		//u while u nginit vrti dok ne dobijes po id da je transakcija sacuvana kao success ili failed
		

	}
	
	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<?> exceptionHandlerHttpError(HttpClientErrorException ex) {
		String body = ex.getResponseBodyAsString();
		RestClientExceptionInfo info = new RestClientExceptionInfo(); 
		
		
		if(RestClientExceptionInfo.parse(body) == null) {
			//ova aplikacija je uzrok exceptiona
			//priprema se exception za propagiranje dalje i loguje se
			info.setOrigin(errorOriginName);
			info.setInfo(body);
		}
		else {
			info.setOrigin(RestClientExceptionInfo.parse(body).getOrigin() );
			info.setInfo(RestClientExceptionInfo.parse(body).getInfo() );
		}
		logger.error("HttpClientErrorException, info:" + RestClientExceptionInfo.toJSON(info));
		
		
		return ResponseEntity.status(ex.getStatusCode()).body(RestClientExceptionInfo.toJSON(info));
	}
}