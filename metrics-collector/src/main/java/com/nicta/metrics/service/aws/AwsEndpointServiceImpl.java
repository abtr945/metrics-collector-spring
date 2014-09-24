package com.nicta.metrics.service.aws;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.nicta.metrics.utility.aws.AwsProperties;
import com.nicta.metrics.utility.aws.AwsRegions;

/**
 * Implements operations related to AWS Endpoints 
 * (i.e. the selected Region for AWS Services)
 * 
 * @author anbinhtran
 *
 */
@Service
public class AwsEndpointServiceImpl implements AwsEndpointService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AwsEndpointServiceImpl.class);
	
	@Override
	public String getSelectedAwsRegion() {
		return getRegion();	
	}

	@Override
	public void setAwsRegion(String region) throws IllegalArgumentException, IOException {
		
		LOGGER.debug("Update the AWS Region for use with AWS Services API");
		
		// Check for valid AWS Region
		if (GenericValidator.isBlankOrNull(region)) {
			LOGGER.debug("The provided AWS Region is blank or null.");
			throw new IllegalArgumentException("The provided AWS Region is blank or null.");
		}
		if (!AwsRegions.contains(region)) {
			LOGGER.debug("The provided AWS Region is in invalid format.");
			throw new IllegalArgumentException("The provided AWS Region is in invalid format.");
		}
		
		/* By default, look for AWS Region setting at:
		 * <USER_HOME>/.cloudwatch_metrics_collector/AWSRegion.txt
		 */
		File regionFile = new File(AwsProperties.getRegionFilePath());
		
		if (!regionFile.exists()) {
			// Create the file if it does not exist
			regionFile.getParentFile().mkdirs();
			regionFile.createNewFile();
		}
		// Write to (or Overwrite) the Region config file
		Writer writer = new BufferedWriter(new FileWriter(regionFile, false));
		writer.write(region + "\n");
		writer.flush();
		writer.close();
		
	}
	
	@Override
	public String getEndpoint_CloudWatch() {
		String region = getRegion();
		if (region != null) {
			return "monitoring." + region + ".amazonaws.com";
		}
		return null;
	}

	@Override
	public String getEndpoint_EC2() {
		String region = getRegion();
		if (region != null) {
			return "ec2." + region + ".amazonaws.com";
		}
		return null;
	}

	@Override
	public String getEndpoint_AutoScaling() {
		String region = getRegion();
		if (region != null) {
			return "autoscaling." + region + ".amazonaws.com";
		}
		return null;
	}

	private String getRegion() {
		
		String region;
		
		/* By default, look for AWS Region setting at:
		 * <USER_HOME>/.cloudwatch_metrics_collector/AWSRegion.txt
		 */
		File regionFile = new File(AwsProperties.getRegionFilePath());
		if (regionFile.exists()) {
			// Read the first line of the file to get the AWS Region name (i.e. ap-southeast-2)
			try {
				BufferedReader reader = new BufferedReader(new FileReader(regionFile));
				region = reader.readLine();
				region = region.replace("\n", "").replace("\r", "");
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				region = null;
			}
		} else {
			// If the Region file does not exist, look for AWS Default Region settings in System Environment
			region = AwsProperties.getAwsDefaultRegion();
		}
		
		// If we can get the region from above, check if the region value is of valid format
		if (region != null) {
			if (!AwsRegions.contains(region)) {
				region = null;
			}
		}
		
		return region;
	}

	@Override
	public boolean isRegionSet() {
		if (getRegion() == null) {
			return false;
		} else {
			return true;
		}
	}

}
