package com.nicta.metrics.utility.profile;

import java.util.LinkedHashMap;

/**
 * Define a Profile for Metrics to be collected for Experiments:
 * 2. Experiment involves only ASG
 * 
 * @author anbinhtran
 *
 */
public class MetricProfile_2_ASG extends MetricProfile {

	public MetricProfile_2_ASG() {
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
		
	}
}
