package com.nicta.metrics.utility.aws;

/**
 * An enumeration of all CloudWatch Metrics Namespaces (i.e. Metric categories)
 * which can be collected.
 * 
 * Relevant Namespaces for Aggregated Metrics are: EC2, ELB, AutoScaling, EBS
 * 
 * @author anbinhtran
 *
 */
public enum MetricNamespace {
	
	AutoScaling("AWS/AutoScaling"),
	Billing("AWS/Billing"),
	DynamoDB("AWS/DynamoDB"),
	ElastiCache("AWS/ElastiCache"),
	EBS("AWS/EBS"),
	EC2("AWS/EC2"),
	ELB("AWS/ELB"),
	ElasticMapReduce("AWS/ElasticMapReduce"),
	OpsWorks("AWS/OpsWorks"),
	Redshift("AWS/Redshift"),
	RDS("AWS/RDS"),
	Route53("AWS/Route53"),
	SNS("AWS/SNS"),
	SQS("AWS/SQS"),
	SWF("AWS/SWF"),
	StorageGateway("AWS/StorageGateway");
	
	
	private final String namespace;
	
	private MetricNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	public String getNamespace() {
		return namespace;
	}
	
	public static boolean contains(String test) {
		
		for (MetricNamespace n : MetricNamespace.values()) {
			if (n.getNamespace().equals(test)) {
				return true;
			}
		}
		
		return false;
	}
}
