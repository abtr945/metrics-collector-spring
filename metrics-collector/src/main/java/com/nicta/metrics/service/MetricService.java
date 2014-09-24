package com.nicta.metrics.service;

import java.security.ProviderException;
import java.util.Date;
import java.util.List;

import com.nicta.metrics.domain.Metric;
import com.nicta.metrics.exception.MetricNotFoundException;

/**
 * Define various operations on Metric (create, collect, find, update and delete)
 * 
 * @author anbinhtran
 *
 */
public interface MetricService {
	
	/**
	 * Create a persistent aggregated Metric object using information of the associated Experiment.
	 * For example, to create Metric of CPUUtilization aggregated of the Auto Scaling Group
	 * 
	 * @param experimentId the unique ID of the experiment associated with the metric, whose info will be used for the Metric dimensions
	 * @param name the name of the Metric (i.e. CPUUtilization, NetworkIn, etc.)
	 * @param namespace the Metric category (must be either AWS/EC2, AWS/ELB or AWS/AutoScaling)
	 * @param period the frequency of Metric datapoints, in seconds (i.e. collect data every 60 seconds)
	 * @return the newly created Metric object
	 * @throws IllegalArgumentException if invalid Experiment ID, Metric Name or Namespace provided
	 */
	public Metric createNewMetricFromAutoScalingExperiment(
			Long experimentId, String name, String namespace, Long period) 
					throws IllegalArgumentException;
	
	/**
	 * Create a persistent custom Metric object associated with an Experiment.
	 * 
	 * @param experimentId the unique ID of the experiment associated with the metric
	 * @param name the name of the Metric (i.e. CPUUtilization, NetworkIn, etc.)
	 * @param namespace the Metric category (must be either AWS/EC2, AWS/ELB or AWS/AutoScaling)
	 * @param dimensionKey the Name of the metric Dimension (i.e. InstanceId)
	 * @param dimensionValue the Value of the metric Dimension (i.e. i-12345678)
	 * @param start the Start time for Metric data collection
	 * @param end the End time for Metric data collection
	 * @param period the frequency of Metric datapoints, in seconds (i.e. collect data every 60 seconds)
	 * @return the newly created Metric object
	 * @throws IllegalArgumentException if invalid Experiment ID or Metric parameters provided
	 */
	public Metric createNewCustomMetric(
			Long experimentId, String name, String namespace, 
			String dimensionKey, String dimensionValue, 
			Date start, Date end, Long period) 
					throws IllegalArgumentException;
	
	/**
	 * Call Amazon API to collect the Metric data from CloudWatch, 
	 * and store it in the specified Metric in the database
	 * 
	 * @param metricId the unique ID of the Metric object we want to collect data from CloudWatch
	 * @return the Metric object with its data content from CloudWatch stored within
	 * @throws MetricNotFoundException if a Metric with the given ID cannot be found
	 * @throws ProviderException if AWS Credentials are not set or are invalid
	 * @throws IllegalArgumentException if Metric parameters (i.e. Dimensions) are invalid
	 */
	public Metric collectMetricDataFromCloudWatch(Long metricId) 
			throws MetricNotFoundException, ProviderException, IllegalArgumentException;
	
	/**
	 * Get a Metric object by its ID in the database
	 * 
	 * @param id unique ID of Metric
	 * @return the corresponding Metric Object with the ID
	 */
	public Metric getMetricById(Long id);
	
	/**
	 * Get a list of all Metrics currently stored in the Database
	 * 
	 * @return list of all Metric objects
	 */
	public List<Metric> getAllMetrics();
	
	/**
	 * Delete a Metric object with the specified ID from database
	 * 
	 * @param id unique ID of Metric
	 * @return the deleted Metric object
	 * @throws MetricNotFoundException if a Metric with the given ID cannot be found
	 */
	public Metric deleteMetric(Long id) throws MetricNotFoundException;
	
	/**
	 * Get total number of Metrics recorded in Database
	 * @return total number of Metrics
	 */
	public Long countMetrics();
	
}
