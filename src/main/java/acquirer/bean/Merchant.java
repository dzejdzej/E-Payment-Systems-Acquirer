package acquirer.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Merchant {

	//public static final Map<String, Merchant> KNOWN_MERCHANTS = new HashMap<>();
	
	@Id
	@GeneratedValue
	private int id;
	
	@Column(nullable = false, unique = true)
	private String idMerchant; 
	
	@Column(nullable = false)
	private String passwordMerchant;
	
	public Merchant() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdMerchant() {
		return idMerchant;
	}

	public void setIdMerchant(String idMerchant) {
		this.idMerchant = idMerchant;
	}

	public String getPasswordMerchant() {
		return passwordMerchant;
	}

	public void setPasswordMerchant(String passwordMerchant) {
		this.passwordMerchant = passwordMerchant;
	}
	
//	static {
//		
//		ClassPathResource res = new ClassPathResource("merchants.json");    
//		File file = new File(res.getPath());
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//			List<Merchant> jsonMerchants = (List<Merchant>) mapper.readValue(file, Merchant.class);
//			for (Merchant merchant : jsonMerchants) {
//				KNOWN_MERCHANTS.put(merchant.getId(), merchant);
//			}		
//		} catch (Exception e) { }		
//	}

}

