package com.nicta.metrics.domain;

import java.util.List;

import org.jfree.data.xy.XYDataset;

/**
 * Class containing all required data for plotting a Line Chart for Metrics
 * 
 * @author anbinhtran
 *
 */
public class ChartData {

	private String chartName;
	private String unit;
	private XYDataset dataset;
	private List<String> timeAxis;
	

	public ChartData(String chartName, String unit, XYDataset dataset,
			List<String> timeAxis) {
		this.chartName = chartName;
		this.unit = unit;
		this.dataset = dataset;
		this.timeAxis = timeAxis;
	}
	
	
	public String getChartName() {
		return chartName;
	}
	public void setChartName(String chartName) {
		this.chartName = chartName;
	}
	public XYDataset getDataset() {
		return dataset;
	}
	public void setDataset(XYDataset dataset) {
		this.dataset = dataset;
	}
	public List<String> getTimeAxis() {
		return timeAxis;
	}
	public void setTimeAxis(List<String> timeAxis) {
		this.timeAxis = timeAxis;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
}
