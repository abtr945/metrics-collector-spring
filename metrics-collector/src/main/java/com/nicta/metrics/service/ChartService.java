package com.nicta.metrics.service;

import java.util.List;

import org.jfree.chart.JFreeChart;

import com.nicta.metrics.domain.Metric;

/**
 * Define services for plotting Charts
 * 
 * @author anbinhtran
 *
 */
public interface ChartService {

	/**
	 * Create a JFreeChart Line Chart, plotting the given Metric data
	 * 
	 * @param metric the Metric data we want to plot a chart out of
	 * @param statistics the list of Statistics (SUM, AVERAGE, MAXIMUM, MINIMUM) we want to plot on the graph
	 * @return a JFreeChart object representing the Line Chart
	 */
	public JFreeChart createLineChart(Metric metric, List<String> statistics);
	
}
