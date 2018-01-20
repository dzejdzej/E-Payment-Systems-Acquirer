package acquirer.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;

@Entity
public class Transakcija implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private int transactionIdMerchant; 

    @Column(nullable = false)
    private StanjeTransakcije stanje;
    
    @Column(nullable = false)
    private Date vreme; 

    @ManyToOne(fetch = FetchType.EAGER)
    private Merchant merchant; 
    
    @Digits(integer=10, fraction=2)
    private BigDecimal amount; 
    
    @Column(nullable = true)
    private String errorUrl; 
    
    @Column(nullable = true)
    private String failedUrl; 
    
    @Column(nullable = true)
    private String successUrl;
    
    @Column(nullable = true)
    private String razlog; 
    
    public Transakcija(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTransactionIdMerchant() {
		return transactionIdMerchant;
	}

	public void setTransactionIdMerchant(int transactionIdMerchant) {
		this.transactionIdMerchant = transactionIdMerchant;
	}

	public StanjeTransakcije getStanje() {
		return stanje;
	}

	public void setStanje(StanjeTransakcije stanje) {
		this.stanje = stanje;
	}

	public Date getVreme() {
		return vreme;
	}

	public void setVreme(Date vreme) {
		this.vreme = vreme;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getErrorUrl() {
		return errorUrl;
	}

	public void setErrorUrl(String errorUrl) {
		this.errorUrl = errorUrl;
	}

	public String getFailedUrl() {
		return failedUrl;
	}

	public void setFailedUrl(String failedUrl) {
		this.failedUrl = failedUrl;
	}

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public String getRazlog() {
		return razlog;
	}

	public void setRazlog(String razlog) {
		this.razlog = razlog;
	}
}
