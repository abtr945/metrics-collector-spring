package com.nicta.metrics.domain;

import java.util.List;

import com.amazonaws.services.cloudwatch.model.Datapoint;

/**
 * Java POJO class representing the JSON data of a CloudWatch Metric
 * 
 * @author anbinhtran
 *
 */
public class MetricJson {

	public String Label;
	public String Dimensions;
	public List<Datapoint> Datapoints;
	
	public MetricJson(String Label, String Dimensions, List<Datapoint> Datapoints) {
		this.Label = Label;
		
		this.Dimensions = Dimensions;
		
		// Filter the Dimensions to get rid of Name= and Value=
		this.Dimensions = this.Dimensions.replaceAll("Name=", "");
		this.Dimensions = this.Dimensions.replaceAll("Value=", "");
		this.Dimensions = this.Dimensions.replaceAll(",", ":");
		
		this.Datapoints = Datapoints;
	}
	
	public String getLabel() {
		return Label;
	}
	
	public void setLabel(String Label) {
		this.Label = Label;
	}
	
	public String getDimensions() {
		return Dimensions;
	}

	public void setDimensions(String dimensions) {
		Dimensions = dimensions;
	}

	public List<Datapoint> getDatapoints() {
		return Datapoints;
	}
	
	public void setDatapoints(List<Datapoint> Datapoints) {
		this.Datapoints = Datapoints;
	}
	
	
}
