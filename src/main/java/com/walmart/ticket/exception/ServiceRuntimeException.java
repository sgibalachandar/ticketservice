package com.walmart.ticket.exception;

public class ServiceRuntimeException extends RuntimeException{

	private static final long serialVersionUID = 20160429L;
	private ErrorCategory errorCategory;
	private ServiceErrorCode errorCode;

	public ErrorCategory getErrorCategory() {
		return errorCategory;
	}
	public void setErrorCategory(ErrorCategory errorCategory) {
		this.errorCategory = errorCategory;
	}
	public ServiceErrorCode getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(ServiceErrorCode errorCode) {
		this.errorCode = errorCode;
	}
	public ServiceRuntimeException(ErrorCategory errorCategory,ServiceErrorCode errorCode){
		super(errorCode.getMessage());
		this.errorCategory = errorCategory;
		this.errorCode = errorCode;
	}
	public ServiceRuntimeException(ErrorCategory errorCategory,ServiceErrorCode errorCode,Throwable cause){
		super(errorCode.getMessage(),cause);
		this.errorCategory = errorCategory;
		this.errorCode = errorCode;
	}

}
