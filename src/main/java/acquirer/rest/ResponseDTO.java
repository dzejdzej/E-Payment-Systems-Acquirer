package acquirer.rest;

public class ResponseDTO {

	private Boolean success; 
	private String errorInfo; 
	private String reason; 
	private String errorUrl; 
	private String failedUrl; 
	private String successUrl; 
	
	private int transactionIdMerchant; 
	
	public ResponseDTO() {
		
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

	public int getTransactionIdMerchant() {
		return transactionIdMerchant;
	}

	public void setTransactionIdMerchant(int transactionIdMerchant) {
		this.transactionIdMerchant = transactionIdMerchant;
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
}
