package acquirer.rest;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import acquirer.bean.Merchant;
import acquirer.bean.StanjeTransakcije;
import acquirer.bean.Transakcija;
import acquirer.repository.MerchantRepository;
import acquirer.repository.TransakcijaRepository;


@RestController
@RequestMapping("/test")
public class TestController {

	
	@PostConstruct
	public void init() {
		Merchant merchant = new Merchant(); 
		merchant.setIdMerchant("1234567891");
		merchant.setPasswordMerchant("Password1");
		Transakcija transakcija = new Transakcija();
		transakcija.setAmount(new BigDecimal(2000.0));
		transakcija.setErrorUrl("http://blic.rs");
		transakcija.setFailedUrl("http://sbb.rs");
		transakcija.setSuccessUrl("http://google.com");
		transakcija.setMerchant(merchant);
		transakcija.setRazlog("kek");
		transakcija.setStanje(StanjeTransakcije.Zapoceta);
		transakcija.setTransactionIdMerchant(1);
		transakcija.setVreme(new Date());
		
		merchant = merchantRepository.save(merchant);
		transakcija.setMerchant(merchant);
		transakcijaRepository.save(transakcija);
		
	}
	
	@Autowired 
	private TransakcijaRepository transakcijaRepository;
	
	@Autowired
	private MerchantRepository merchantRepository; 

	@RequestMapping(method = RequestMethod.GET, value = "/fill-acquirer-database")
	public ResponseEntity<?> fillAquirerDatabase() {
		
		Merchant merchant1 = new Merchant(); 
		merchant1.setIdMerchant("1234567890");
		merchant1.setPasswordMerchant("jdfakjfakjfa");
		merchantRepository.save(merchant1);
		
		Merchant merchant2 = new Merchant(); 
		merchant2.setIdMerchant("id1");
		merchant2.setPasswordMerchant("password1");
		merchantRepository.save(merchant2);
		
		Merchant merchant3 = new Merchant(); 
		merchant3.setIdMerchant("id1");
		merchant3.setPasswordMerchant("password1");
		merchantRepository.save(merchant3);
		
		System.out.println("AQUIRER DATABASE FILLED");
		
		return new ResponseEntity<>(HttpStatus.OK);
	}	
}
