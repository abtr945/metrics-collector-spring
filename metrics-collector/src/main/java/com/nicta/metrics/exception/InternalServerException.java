package com.nicta.metrics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6982412195483202148L;

	public InternalServerException() {}

    public InternalServerException(String message)
    {
       super(message);
    }
    
}
