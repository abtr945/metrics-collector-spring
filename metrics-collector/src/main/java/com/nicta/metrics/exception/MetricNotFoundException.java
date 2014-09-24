package com.nicta.metrics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception to be thrown when a Metric cannot be found in the Persistence level
 * 
 * @author anbinhtran
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class MetricNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5876644031323726979L;

	public MetricNotFoundException() {}

    public MetricNotFoundException(String message)
    {
       super(message);
    }
    
}
