package com.nicta.metrics.utility.aws;

import java.util.Comparator;

import com.amazonaws.services.cloudwatch.model.Datapoint;

/**
 * Comparator for 2 AWS CloudWatch Datapoint.
 * 
 * The order is determined by the timestamp of the 2 datapoints.
 * 
 * @author anbinhtran
 *
 */
public class DatapointComparator implements Comparator<Datapoint> {

	@Override
	public int compare(Datapoint arg0, Datapoint arg1) {
		return arg0.getTimestamp().compareTo(arg1.getTimestamp());
	}

}
