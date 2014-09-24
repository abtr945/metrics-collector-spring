package com.nicta.metrics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadParameterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3245862433929858426L;

	public BadParameterException() {}

    public BadParameterException(String message)
    {
       super(message);
    }
    
}
