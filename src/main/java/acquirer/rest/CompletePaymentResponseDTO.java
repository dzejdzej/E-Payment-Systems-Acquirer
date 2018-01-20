package acquirer.rest;

import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CompletePaymentResponseDTO {

	private Boolean success; 
	private String errorInfo; 
	private String reason; 
	private int issuerTransactionId; 
	private Date issuerTimestamp; 
	private int acquirerOrderId; 
	private Date acquirerTimestamp; 
	
	private String errorURL;
	private String failedURL;
	private String successURL;
	
	public static CompletePaymentResponseDTO parse(String content) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			
			//JSON from String to Object
			CompletePaymentResponseDTO obj = mapper.readValue(content, CompletePaymentResponseDTO.class);
			return obj; 
		}catch(Exception e) {
			e.printStackTrace();
			return null; 
		}
	}
	
	public String toJSON() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
			
		}catch(Exception e) {
			e.printStackTrace();
			return null; 
		}
	}
	
	public CompletePaymentResponseDTO() {
		
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getIssuerTransactionId() {
		return issuerTransactionId;
	}

	public void setIssuerTransactionId(int issuerTransactionId) {
		this.issuerTransactionId = issuerTransactionId;
	}

	public Date getIssuerTimestamp() {
		return issuerTimestamp;
	}

	public void setIssuerTimestamp(Date issuerTimestamp) {
		this.issuerTimestamp = issuerTimestamp;
	}

	public int getAcquirerOrderId() {
		return acquirerOrderId;
	}

	public void setAcquirerOrderId(int acquirerOrderId) {
		this.acquirerOrderId = acquirerOrderId;
	}

	public Date getAcquirerTimestamp() {
		return acquirerTimestamp;
	}

	public void setAcquirerTimestamp(Date acquirerTimestamp) {
		this.acquirerTimestamp = acquirerTimestamp;
	}

	public String getErrorURL() {
		return errorURL;
	}

	public void setErrorURL(String errorURL) {
		this.errorURL = errorURL;
	}

	public String getFailedURL() {
		return failedURL;
	}

	public void setFailedURL(String failedURL) {
		this.failedURL = failedURL;
	}

	public String getSuccessURL() {
		return successURL;
	}

	public void setSuccessURL(String successURL) {
		this.successURL = successURL;
	}
}
