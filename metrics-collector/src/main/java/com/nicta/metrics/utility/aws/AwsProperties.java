package com.nicta.metrics.utility.aws;

/**
 * Get several AWS Account Properties from the System Environment.
 * 
 * Might requires at least these following SysEnv attributes to be set:
 * AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY, AWS_DEFAULT_REGION
 * 
 * @author anbinhtran
 *
 */
public class AwsProperties {
	
	public static String getCredentialsFilePath() {
		return "/tmp/.cloudwatch_metrics_collector/AWSCredentials.txt";
	}
	
	public static String getRegionFilePath() {
		return "/tmp/.cloudwatch_metrics_collector/AWSRegion.txt";
	}

	public static String getAwsAccessKeyId() {
		return System.getenv("AWS_ACCESS_KEY_ID");
	}
	
	public static String getAwsSecretAccessKey() {
		return System.getenv("AWS_SECRET_ACCESS_KEY");
	}
	
	public static String getAwsDefaultRegion() {
		return System.getenv("AWS_DEFAULT_REGION");
	}
	
	public static String getAwsSshKeyId() {
		return System.getenv("AWS_SSH_KEY_ID");
	}
	
	public static String getAwsIdentity() {
		return System.getenv("AWS_IDENTITY");
	}
	
}
