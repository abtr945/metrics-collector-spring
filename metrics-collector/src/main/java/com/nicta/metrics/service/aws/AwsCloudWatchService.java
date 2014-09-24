package com.nicta.metrics.service.aws;

import java.security.ProviderException;

import com.nicta.metrics.domain.Metric;

/**
 * Define operations related to Amazon CloudWatch
 * 
 * @author anbinhtran
 *
 */
public interface AwsCloudWatchService {

	/**
	 * Collect data for a single Metric from Amazon CloudWatch,
	 * using the parameters of the provided Metric object
	 * 
	 * @param metric the Metric to collect CloudWatch data
	 * @throws ProviderException if AWS Credentials are not provided or are invalid
	 * @throws IllegalArgumentException if the metric Dimensions parameter is invalid
	 */
	public void collectMetric(Metric metric) throws ProviderException, IllegalArgumentException;
	
}
