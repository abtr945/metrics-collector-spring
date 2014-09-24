package com.nicta.metrics.domain;

import com.nicta.metrics.utility.aws.AwsRegions;

/**
 * Statistics class hold summary information
 * about the currently selected AWS Region,
 * as well as Total number of recorded Experiments and Metrics collected.
 * 
 * @author anbinhtran
 *
 */
public class Statistics {

	private String selectedRegion;
	private Long totalExperiments;
	private Long totalMetrics;
	private AwsRegions[] regions;
	
	public Statistics() {}
	
	public Statistics(String selectedRegion, Long totalExperiments,
			Long totalMetrics) {
		this.selectedRegion = selectedRegion;
		this.totalExperiments = totalExperiments;
		this.totalMetrics = totalMetrics;
		this.regions = AwsRegions.values();
	}
	
	public String getSelectedRegion() {
		return selectedRegion;
	}
	
	public void setSelectedRegion(String selectedRegion) {
		this.selectedRegion = selectedRegion;
	}
	
	public Long getTotalExperiments() {
		return totalExperiments;
	}
	
	public void setTotalExperiments(Long totalExperiments) {
		this.totalExperiments = totalExperiments;
	}
	
	public Long getTotalMetrics() {
		return totalMetrics;
	}
	
	public void setTotalMetrics(Long totalMetrics) {
		this.totalMetrics = totalMetrics;
	}

	public AwsRegions[] getRegions() {
		return regions;
	}

	public void setRegions(AwsRegions[] regions) {
		this.regions = regions;
	}
	
}
