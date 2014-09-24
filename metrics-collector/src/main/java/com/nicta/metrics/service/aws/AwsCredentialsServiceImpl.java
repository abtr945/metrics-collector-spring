package com.nicta.metrics.service.aws;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.nicta.metrics.utility.aws.AwsProperties;

/**
 * Implements operations related to Amazon AWS Credentials (authentication for AWS API services)
 * 
 * @author anbinhtran
 *
 */
@Service
public class AwsCredentialsServiceImpl implements AwsCredentialsService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AwsCredentialsServiceImpl.class);
	
	@Override
	public AWSCredentials getCredentials() {
		
		LOGGER.debug("Get the AWS Credentials from Configuration file or System environment");
		
		AWSCredentials credentials;
		
		/* By default, look for AWS Credentials file at:
		 * <USER_HOME>/.cloudwatch_metrics_collector/AWSCredentials.txt
		 */
		File credentialsFile = new File(AwsProperties.getCredentialsFilePath());
		
		if (credentialsFile.exists()) {
			// Create Credentials object using the information in the Credentials file
			try {
				credentials = new PropertiesCredentials(credentialsFile);
			} catch (IllegalArgumentException | IOException e) {
				LOGGER.debug("IOException occur when reading AWS Credentials from Configuration file; return NULL");
				credentials = null;
			}
		} else {
			// If Credentials file not exists, look for AWS Credentials set in System Environment
			String awsAccessKeyId = AwsProperties.getAwsAccessKeyId();
			String awsSecretAccessKey = AwsProperties.getAwsSecretAccessKey();
			
			if (GenericValidator.isBlankOrNull(awsAccessKeyId) || 
					GenericValidator.isBlankOrNull(awsSecretAccessKey)) {
				// If cannot get AWS Credentials in System Env either, return null
				LOGGER.debug("Cannot get AWS Credentials from both sources; return NULL");
				credentials = null;
			} else {
				// Else, use it to create Credentials
				credentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);
			}
		}
		
		return credentials;
	}

	@Override
	public void createOrUpdateCredentials(String awsAccessKeyId,
			String awsSecretAccessKey) throws IllegalArgumentException, IOException {
		
		LOGGER.debug("Create a new AWS Credentials conf. file or update existing Credentials");
		
		// Check for valid AWS ID and secret key
		if (GenericValidator.isBlankOrNull(awsAccessKeyId) || 
				GenericValidator.isBlankOrNull(awsSecretAccessKey)) {
			LOGGER.debug("The provided AWS Access Key ID and/or Secret Access Key are invalid.");
			throw new IllegalArgumentException("The provided AWS Access Key ID and/or Secret Access Key are invalid.");
		}
		
		/* By default, look for AWS Credentials file at:
		 * <USER_HOME>/.cloudwatch_metrics_collector/AWSCredentials.txt
		 */
		File credentialsFile = new File(AwsProperties.getCredentialsFilePath());
		
		if (!credentialsFile.exists()) {
			// Create the file if it does not exist
			credentialsFile.getParentFile().mkdirs();
			credentialsFile.createNewFile();
		}
		// Write to (or Overwrite) the Credentials config file
		Writer writer = new BufferedWriter(new FileWriter(credentialsFile, false));
		writer.write("accessKey=" + awsAccessKeyId + "\n");
		writer.write("secretKey=" + awsSecretAccessKey + "\n");
		writer.flush();
		writer.close();
		
	}

	@Override
	public boolean isCredentialsSet() {
		if (getCredentials() == null) {
			return false;
		} else {
			return true;
		}
	}
}
