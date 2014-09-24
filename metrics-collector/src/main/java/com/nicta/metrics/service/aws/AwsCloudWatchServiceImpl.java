package com.nicta.metrics.service.aws;

import java.security.ProviderException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.google.gson.Gson;
import com.nicta.metrics.domain.Metric;
import com.nicta.metrics.utility.aws.DatapointComparator;

/**
 * Implements the operations on Amazon CloudWatch API
 * 
 * @author anbinhtran
 *
 */
@Service
public class AwsCloudWatchServiceImpl implements AwsCloudWatchService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AwsCloudWatchServiceImpl.class);
    
    @Autowired
    AwsCredentialsService credentialsService;
    
    @Autowired
    AwsEndpointService endpointService;
    
	@Override
	public void collectMetric(Metric metric) throws ProviderException, IllegalArgumentException {
		
		LOGGER.debug("Collect a single Metric data from CloudWatch");
		
		// Get the AWS Credentials and CloudWatch endpoint
		AWSCredentials credentials = credentialsService.getCredentials();
		String endpoint = endpointService.getEndpoint_CloudWatch();
		if (credentials == null || endpoint == null) {
			LOGGER.debug("AWS Provider credentials and/or region not specified.");
			throw new ProviderException("AWS Provider credentials and/or region not specified.");
		}
		
		// Create AmazonCloudWatch client
		AmazonCloudWatch client = new AmazonCloudWatchClient(credentials);
		client.setEndpoint(endpoint);
		
		// Parse the Dimensions
		Dimension dim = null;
		Pattern p = Pattern.compile("^Name=([^,]+),Value=(.+)$");
		Matcher m = p.matcher(metric.getDimensions());
		if (m.find()) {
			String dimName = m.group(1);
			String dimValue = m.group(2);
			dim = new Dimension();
			dim.withName(dimName).withValue(dimValue);
		} else {
			LOGGER.debug("The Dimensions of the specified Metric is in invalid format.");
			throw new IllegalArgumentException("The Dimensions of the specified Metric is in invalid format.");
		}
		
		// Parse the Statistics to a List
		List<String> statisticsList = Arrays.asList(metric.getStatistics().split("\\s*,\\s*"));
		
		// Collect the CloudWatch metrics using the stored parameters
		GetMetricStatisticsRequest request = new GetMetricStatisticsRequest();
		request.withMetricName(metric.getName()).withNamespace(metric.getNamespace()).withDimensions(dim);
		request.withStatistics(statisticsList).withPeriod(metric.getPeriod().intValue());
		request.withStartTime(metric.getStartTime()).withEndTime(metric.getEndTime());
		
		GetMetricStatisticsResult result = client.getMetricStatistics(request);
		List<Datapoint> dps = result.getDatapoints();
		
		// Sort the datapoint list by Timestamp
		Collections.sort(dps, new DatapointComparator());
		
		// Get the result to a JSON format
		Gson gson = new Gson();
		metric.setJsonData(gson.toJson(dps));
		
	}

}
