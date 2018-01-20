package acquirer.rest;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import acquirer.bean.Merchant;
import acquirer.bean.Transakcija;



public class TransactionDTO {

	private Merchant merchant;

	private int transactionIdMerchant;

	private Date vreme;

	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal amount;
	
	private String errorUrl; 
	
	private String successUrl; 
	
	private String failedUrl; 
	
	public TransactionDTO() {
		
	}

	public TransactionDTO(Transakcija transakcija) {
		this.amount = transakcija.getAmount(); 
		this.errorUrl = transakcija.getErrorUrl(); 
		this.failedUrl = transakcija.getFailedUrl(); 
		this.merchant = transakcija.getMerchant(); 
		this.successUrl = transakcija.getSuccessUrl(); 
		this.transactionIdMerchant = transakcija.getTransactionIdMerchant(); 
		this.vreme = transakcija.getVreme(); 
	}
	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}

	public int getTransactionIdMerchant() {
		return transactionIdMerchant;
	}

	public void setTransactionIdMerchant(int transactionIdMerchant) {
		this.transactionIdMerchant = transactionIdMerchant;
	}

	public Date getVreme() {
		return vreme;
	}

	public void setVreme(Date vreme) {
		this.vreme = vreme;
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

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public String getFailedUrl() {
		return failedUrl;
	}

	public void setFailedUrl(String failedUrl) {
		this.failedUrl = failedUrl;
	}
}
