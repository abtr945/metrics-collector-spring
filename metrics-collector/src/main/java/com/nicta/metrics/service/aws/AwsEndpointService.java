package com.nicta.metrics.service.aws;

import java.io.IOException;

/**
 * Define operations related to AWS Endpoints 
 * (i.e. Region of various AWS services, for example ap-southeast-2)
 * 
 * @author anbinhtran
 *
 */
public interface AwsEndpointService {

	/**
	 * Check if an AWS Region is already set
	 * 
	 * @return true if AWS Regions is set, false otherwise
	 */
	public boolean isRegionSet();
	
	/**
	 * Get the currently selected AWS Region (i.e. ap-southeast-2)
	 * 
	 * @return the currently selected AWS Region
	 */
	public String getSelectedAwsRegion();
	
	/**
	 * Set or update the AWS Region
	 * 
	 * @param region the new AWS Region we want to set (i.e. ap-southeast-2)
	 * @throws IllegalArgumentException if the provided Region is invalid
	 * @throws IOException if error occur when accessing AWS Configuration file
	 */
	public void setAwsRegion(String region) throws IllegalArgumentException, IOException;
	
	/**
	 * Get the AWS Service Endpoint for CloudWatch
	 * (i.e. monitoring.ap-southeast-2.amazonaws.com)
	 * 
	 * @return the CloudWatch endpoint
	 */
	public String getEndpoint_CloudWatch();
	
	/**
	 * Get the AWS Service Endpoint for EC2
	 * (i.e. ec2.ap-southeast-2.amazonaws.com)
	 * 
	 * @return the EC2 endpoint
	 */
	public String getEndpoint_EC2();
	
	/**
	 * Get the AWS Service Endpoint for Auto Scaling Group
	 * (i.e. autoscaling.ap-southeast-2.amazonaws.com)
	 * 
	 * @return the Auto Scaling Group endpoint
	 */
	public String getEndpoint_AutoScaling();
	
}
