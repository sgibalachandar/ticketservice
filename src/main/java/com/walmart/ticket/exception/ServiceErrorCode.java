package com.walmart.ticket.exception;

public enum ServiceErrorCode {

	INTERNAL_SERVER_ERROR(500,"Unknow Error"),
	CUSTOMER_ALREADY_EXISTS(400,"Customer Aleready Exists"),
	LEVEL_DOES_NOT_EXIST(400,"Invalid level id"),
	CUSTOMER_NOT_REGISTERED(400,"Customer not registered"),
	SEATS_UNAVAILABLE(400,"Sorry!,seats become unavailable"),
	UNABLE_TO_HOLD_SEATS(400,"Unable to hold seats,Please contact customer support"),
	UNABLE_TO_RESERVE_SEATS(400,"Unable to reserve seats"),
	CUSTOMER_REACHED_LIMIT_TO_HOLD_SEATS(400,"Customer has reached limit allowed to hold"),
	INVALID_INPUT_FOR_LEVEL(400,"Invalid input levels"),
	HOLD_EXPIRED(400,"Hold expired or ticket was not held");

	private int httpstatus;
	private String message;
	ServiceErrorCode(int httpstatus,String message){
		this.httpstatus = httpstatus;
		this.message = message;
	}
	public int getHttpstatus() {
		return httpstatus;
	}
	public void setHttpstatus(int httpstatus) {
		this.httpstatus = httpstatus;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
