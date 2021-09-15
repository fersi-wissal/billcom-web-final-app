package com.billcom.app.exception;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

	int status;
	String message;
	long timestamp;
	String path;
    Map<String,String> validationError;

    public ApiError(int status, String message,  String path) {
		this.status = status;
		this.message = message;
		this.timestamp = new Date().getTime();
		this.path = path;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Map<String, String> getValidationError() {
		return validationError;
	}

	public void setValidationError(Map<String, String> validationError) {
		this.validationError = validationError;
	}
	
	
	
}
