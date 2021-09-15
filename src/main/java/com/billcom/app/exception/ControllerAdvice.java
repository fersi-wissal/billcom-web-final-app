package com.billcom.app.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiError handleNotFoundException(NotFoundException exception, HttpServletRequest request) {
		return new ApiError(404, exception.getMessage(), request.getServletPath());
		

	}

	@ExceptionHandler(ForbiddenException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ApiError handleForbiddenException(ForbiddenException exception, HttpServletRequest request) {
		return new ApiError(401, exception.getMessage(), request.getServletPath());

	}
	@ExceptionHandler(EmptyListException.class)
	public ApiError handleEmptyListException(EmptyListException exception, HttpServletRequest request) {
		return new ApiError(405, exception.getMessage(), request.getServletPath());

	}
	@ExceptionHandler(AlreadyExistsException.class)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public ApiError handleAlreadyExistsException(AlreadyExistsException exception, HttpServletRequest request) {
		return new ApiError(406, exception.getMessage(), request.getServletPath());
		

	}
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public ApiError illegalArgumentException(IllegalArgumentException exception, HttpServletRequest request) {
		return new ApiError(321, exception.getMessage(), request.getServletPath());
		

	}
}
