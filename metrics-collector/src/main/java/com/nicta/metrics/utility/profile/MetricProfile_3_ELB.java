package com.nicta.metrics.utility.profile;

import java.util.LinkedHashMap;

/**
 * Define a Profile for Metrics to be collected for Experiments:
 * 3. Experiment involves only ELB
 * 
 * @author anbinhtran
 *
 */
public class MetricProfile_3_ELB extends MetricProfile {

	public MetricProfile_3_ELB() {
		metricParams = new LinkedHashMap<String, String>();
		
		// Populate it with the set of metrics we need to collect for Rolling Upgrade
		populateMetrics();
		
		// Default period of collection is 60 seconds
		period = 60l;
	}
	
	protected void populateMetrics() {
		
		// ELB Metrics
		metricParams.put("BackendConnectionErrors", "AWS/ELB");
		metricParams.put("HTTPCode_Backend_2XX", "AWS/ELB");
		metricParams.put("HTTPCode_Backend_4XX", "AWS/ELB");
		metricParams.put("HTTPCode_Backend_5XX", "AWS/ELB");
		metricParams.put("HTTPCode_ELB_4XX", "AWS/ELB");
		metricParams.put("HTTPCode_ELB_5XX", "AWS/ELB");
		metricParams.put("HealthyHostCount", "AWS/ELB");
		metricParams.put("Latency", "AWS/ELB");
		metricParams.put("RequestCount", "AWS/ELB");
		metricParams.put("UnHealthyHostCount", "AWS/ELB");
	}
}
