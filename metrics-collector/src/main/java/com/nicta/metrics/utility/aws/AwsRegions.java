package com.nicta.metrics.utility.aws;

/**
 * Enumerate all Amazon AWS Region names
 * (i.e. us-east-1, ap-southeast-2 etc.)
 * 
 * @author anbinhtran
 *
 */
public enum AwsRegions {
	
	US_EAST_1("us-east-1"),
	US_WEST_1("us-west-1"),
	US_WEST_2("us-west-2"),
	EU_WEST_1("eu-west-1"),
	AP_SOUTHEAST_1("ap-southeast-1"),
	AP_SOUTHEAST_2("ap-southeast-2"),
	AP_NORTHEAST_1("ap-northeast-1"),
	SA_EAST_1("sa-east-1");
	
	private final String value;
	
	AwsRegions(String v) {
		value = v;
	}
	
	public String value() {
		return value;
	}
	
	public static boolean contains(String test) {

	    for (AwsRegions r : AwsRegions.values()) {
	        if (r.value().equals(test)) {
	            return true;
	        }
	    }

	    return false;
	}
}
