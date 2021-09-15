package com.billcom.app.exception;


import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class EmptyListException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public EmptyListException(String message) {
		super(message);
	}
	
}