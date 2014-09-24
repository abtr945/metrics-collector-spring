package com.nicta.metrics.utility.profile;

import java.util.LinkedHashMap;

/**
 * Define a Profile for Metrics to be collected for Experiments:
 * 1. Experiment involves both ASG and ELB
 * 
 * @author anbinhtran
 *
 */
public class MetricProfile_1_ASGandELB extends MetricProfile {

	public MetricProfile_1_ASGandELB() {
		metricParams = new LinkedHashMap<String, String>();
		
		// Populate it with the set of metrics we need to collect for Rolling Upgrade
		populateMetrics();
		
		// Default period of collection is 60 seconds
		period = 60l;
	}
	
	protected void populateMetrics() {
		
		// EC2 Metrics
		metricParams.put("CPUUtilization", "AWS/EC2");
		metricParams.put("DiskReadBytes", "AWS/EC2");
		metricParams.put("DiskReadOps", "AWS/EC2");
		metricParams.put("DiskWriteBytes", "AWS/EC2");
		metricParams.put("DiskWriteOps", "AWS/EC2");
		metricParams.put("NetworkIn", "AWS/EC2");
		metricParams.put("NetworkOut", "AWS/EC2");
		metricParams.put("StatusCheckFailed", "AWS/EC2");
		metricParams.put("StatusCheckFailed_Instance", "AWS/EC2");
		metricParams.put("StatusCheckFailed_System", "AWS/EC2");
		
		// AutoScaling Metrics
		metricParams.put("GroupDesiredCapacity", "AWS/AutoScaling");
		metricParams.put("GroupInServiceInstances", "AWS/AutoScaling");
		metricParams.put("GroupMaxSize", "AWS/AutoScaling");
		metricParams.put("GroupMinSize", "AWS/AutoScaling");
		metricParams.put("GroupPendingInstance", "AWS/AutoScaling");
		metricParams.put("GroupTeminationInstances", "AWS/AutoScaling");
		metricParams.put("GroupTotalInstances", "AWS/AutoScaling");
		
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
