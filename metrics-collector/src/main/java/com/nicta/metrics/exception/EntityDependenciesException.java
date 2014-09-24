package com.nicta.metrics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception to be thrown when a Persistence action violates a Database dependency
 * 
 * (for example, trying to delete an Experiment 
 *  while there are Metrics currently associated with it)
 *  
 * @author anbinhtran
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityDependenciesException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3601632902808648855L;

	public EntityDependenciesException() {}

    public EntityDependenciesException(String message)
    {
       super(message);
    }
    
}
