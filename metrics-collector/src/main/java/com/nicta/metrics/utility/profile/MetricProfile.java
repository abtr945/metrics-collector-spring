package com.nicta.metrics.utility.profile;

import java.util.Map;

/**
 * Abstract Profile class for collection of Metrics to be collected
 * for different types of Experiment
 * 
 * @author anbinhtran
 *
 */
public abstract class MetricProfile {

	protected Map<String, String> metricParams;
	protected Long period;
	
	public Map<String, String> getMetricParams() {
		return metricParams;
	}
	
	public Long getPeriod() {
		return period;
	}
	
	protected abstract void populateMetrics();
	
}
