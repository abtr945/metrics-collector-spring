package com.nicta.metrics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception to be thrown when an Experiment cannot be found in the Persistence level
 * 
 * @author anbinhtran
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExperimentNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6994719464886090407L;

	public ExperimentNotFoundException() {}

    public ExperimentNotFoundException(String message)
    {
       super(message);
    }
    
}
