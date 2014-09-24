package com.nicta.metrics.service;

import java.security.ProviderException;
import java.util.Date;
import java.util.List;

import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nicta.metrics.dao.ExperimentRepository;
import com.nicta.metrics.dao.MetricRepository;
import com.nicta.metrics.domain.Experiment;
import com.nicta.metrics.domain.Metric;
import com.nicta.metrics.exception.MetricNotFoundException;
import com.nicta.metrics.service.aws.AwsCloudWatchService;
import com.nicta.metrics.utility.aws.MetricNamespace;

/**
 * Implements the operations related to Metric
 * 
 * @author anbinhtran
 *
 */
@Service
public class MetricServiceImpl implements MetricService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MetricServiceImpl.class);
	
	@Autowired
	private AwsCloudWatchService awsCloudWatchService;
	
	@Autowired
	private MetricRepository metricRepository;
	
	@Autowired
	private ExperimentRepository experimentRepository;
	
	@Override
	@Transactional
	public Metric createNewMetricFromAutoScalingExperiment(
			Long experimentId, String name, String namespace, Long period) 
					throws IllegalArgumentException {
		LOGGER.debug(
				"Create a Metric object: " + name + 
				" aggregated for either Auto Scaling Group or Load Balancer using the Experiment information");
		
		// Get the associated Experiment object from the ID
		Experiment experiment = experimentRepository.findOne(experimentId);
		
		/* Check for valid parameters */
		
		// The associated Experiment must not be null
		if (experiment == null) {
			LOGGER.debug("No experiment found with id: " + experimentId);
			throw new IllegalArgumentException("Invalid Experiment ID: " + experimentId);
		}
		
		// The associated Experiment must have Data Collection Start Time and End Time set already
		if (experiment.getDataCollectionStartTime() == null || 
				experiment.getDataCollectionEndTime() == null) {
			throw new IllegalArgumentException(
					"The associated Experiment must have Data Collection Start Time and End Time set");
		}
		
		// Namespace must be valid
		if (!MetricNamespace.contains(namespace)) {
			throw new IllegalArgumentException("Metric Namespace is in invalid format");
		}
		
		// Create the Metric "dimensions" parameters
		// using the Experiment Auto Scaling Group Name or Load Balancer Name
		// (since we're creating Aggregated Metrics here).
		//
		// Note that for Aggregated Metrics, Namespace must be either EC2, ELB or AutoScaling
		String dimensions = "";
		if (namespace.equals(MetricNamespace.EC2.getNamespace()) 
				|| namespace.equals(MetricNamespace.AutoScaling.getNamespace())) {
			dimensions = "Name=AutoScalingGroupName," + "Value=" + experiment.getAsgName();
		} else if (namespace.equals(MetricNamespace.ELB.getNamespace())){
			dimensions = "Name=LoadBalancerName," + "Value=" + experiment.getElbName();
		} else {
			throw new IllegalArgumentException (
					"Metrics other than AutoScaling, EC2 and ELB are not supported"
					+ " by this method. You need to manually specify the dimension"
					+ " for these metrics.");
		}
		
		// Create new Metric object
		Metric newMetric = new Metric("aggregated", name, namespace, dimensions, 
				experiment.getDataCollectionStartTime(), experiment.getDataCollectionEndTime(), period);
		
		// Associate the Metric to the Experiment
		experiment.addMetrics(newMetric);
		
		return metricRepository.saveAndFlush(newMetric);
	}

	@Override
	@Transactional
	public Metric createNewCustomMetric(Long experimentId, String name,
			String namespace, String dimensionKey, String dimensionValue,
			Date start, Date end, Long period) throws IllegalArgumentException {
		LOGGER.debug(
				"Create a Metric object: " + name + 
				" aggregated for either Auto Scaling Group or Load Balancer using the Experiment information");
		
		// Get the associated Experiment object from the ID
		Experiment experiment = experimentRepository.findOne(experimentId);
		
		/* Check for valid parameters */
		
		// The associated Experiment must not be null
		if (experiment == null) {
			LOGGER.debug("No experiment found with id: " + experimentId);
			throw new IllegalArgumentException("Invalid Experiment ID: " + experimentId);
		}
		
		// Metric Name, Namespace, Dimensions must not be empty or null
		if (GenericValidator.isBlankOrNull(name) || 
				GenericValidator.isBlankOrNull(namespace) || 
				GenericValidator.isBlankOrNull(dimensionKey) || 
				GenericValidator.isBlankOrNull(dimensionValue)) {
			throw new IllegalArgumentException("Metric Name, Namespace, Dimensions cannot be empty");
		}
		
		// Namespace must be valid
		if (!MetricNamespace.contains(namespace)) {
			throw new IllegalArgumentException("Metric Namespace is in invalid format");
		}
		
		// Start time must be before or equal to End time
		if (!(start.before(end) || start.equals(end))) {
			throw new IllegalArgumentException(
					"Metric collection Start time must be before or equal to End time");
		}
		
		// Period must be positive
		if (period <= 0) {
			throw new IllegalArgumentException ("Metric collection Period must be positive");
		}
		
		// Done checking, compute the Dimensions
		String dimensions = "Name=" + dimensionKey + ",Value=" + dimensionValue;
		
		// Create new Metric object
		Metric newMetric = new Metric("custom", name, namespace, dimensions, start, end, period);
		
		// Associate the new Metric to the Experiment
		experiment.addMetrics(newMetric);
		
		return metricRepository.saveAndFlush(newMetric);
	}
	
	@Override
	@Transactional(
			rollbackFor = { MetricNotFoundException.class, 
					ProviderException.class, 
					IllegalArgumentException.class })
	public Metric collectMetricDataFromCloudWatch(Long metricId) 
			throws MetricNotFoundException, ProviderException, IllegalArgumentException {
		LOGGER.debug("Collecting Metric data from CloudWatch for Metric id: " + metricId);
		
		Metric metric = metricRepository.findOne(metricId);
		
		if (metric == null) {
			LOGGER.debug("No metric found with id: " + metricId);
            throw new MetricNotFoundException();
		}
		
		// Call the CloudWatch service to collect Metric data and store it to the Metric object
		try {
			awsCloudWatchService.collectMetric(metric);
		} catch (ProviderException e) {
			throw e;
		} catch (IllegalArgumentException e) {
			throw e;
		}
		
		return metric;
	}

	@Override
	@Transactional(readOnly = true)
	public Metric getMetricById(Long id) {
		LOGGER.debug("Finding metric by id: " + id);
		return metricRepository.findOne(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Metric> getAllMetrics() {
		LOGGER.debug("Finding all metrics");
		return metricRepository.findAll();
	}

	@Override
	@Transactional(rollbackFor = MetricNotFoundException.class)
	public Metric deleteMetric(Long id) throws MetricNotFoundException {
		LOGGER.debug("Deleting metric with id: " + id);
		
		Metric deleted = metricRepository.findOne(id);
		
		if (deleted == null) {
            LOGGER.debug("No metric found with id: " + id);
            throw new MetricNotFoundException();
        }
        
		// Remove the reference to this Metric in the associated Experiment first
		Experiment exp = deleted.getExperiment();
		exp.getMetrics().remove(deleted);
		
        metricRepository.delete(deleted);
        return deleted;
	}

	@Override
	@Transactional(readOnly = true)
	public Long countMetrics() {
		return metricRepository.count();
	}

	/**
	 * This setter method should be used only by unit tests
	 * 
	 * @param metricRepository the MetricRepository to set
	 */
	public void setMetricRepository(MetricRepository metricRepository) {
		this.metricRepository = metricRepository;
	}
	
	/**
	 * This setter method should be used only by unit tests
	 * 
	 * @param experimentRepository the ExperimentRepository to set
	 */
	public void setExperimentRepository(ExperimentRepository experimentRepository) {
		this.experimentRepository = experimentRepository;
	}
	
	/**
	 * This setter method should be used only by unit tests
	 * 
	 * @param awsCloudWatchService the AwsCloudWatchService to set
	 */
	public void setAwsCloudWatchService(AwsCloudWatchService awsCloudWatchService) {
		this.awsCloudWatchService = awsCloudWatchService;
	}
}
