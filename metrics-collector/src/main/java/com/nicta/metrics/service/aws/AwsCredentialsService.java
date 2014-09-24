package com.nicta.metrics.service.aws;

import java.io.IOException;

import com.amazonaws.auth.AWSCredentials;

/**
 * Define operations related to AWS Credentials (authentication for various AWS services)
 * 
 * @author anbinhtran
 *
 */
public interface AwsCredentialsService {

	/**
	 * Get the current AWS credentials 
	 *
	 * @return the current AWS credentials
	 */
	public AWSCredentials getCredentials();
	
	/**
	 * Create a new AWS Credentials configuration file,
	 * or update existing AWS Credentials to new values
	 * 
	 * @param awsAccessKeyId the new AWS Access Key ID
	 * @param awsSecretAccessKey the new AWS Secret Key 
	 * @throws IllegalArgumentException if the provided credentials are invalid
	 * @throws IOException if error occurs when accessing the Credentials Configuration file
	 */
	public void createOrUpdateCredentials(String awsAccessKeyId, String awsSecretAccessKey) 
			throws IllegalArgumentException, IOException;
	
	/**
	 * Check if AWS Credentials are already set
	 * 
	 * @return true if AWS Credentials are already set, false otherwise
	 */
	public boolean isCredentialsSet();
}
