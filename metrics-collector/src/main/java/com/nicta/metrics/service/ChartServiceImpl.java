package com.nicta.metrics.service;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nicta.metrics.domain.ChartData;
import com.nicta.metrics.domain.Metric;

/**
 * Implementation of Chart Services
 * 
 * @author anbinhtran
 *
 */
@Service
public class ChartServiceImpl implements ChartService {

	@Override
	public JFreeChart createLineChart(Metric metric, List<String> statistics) {
		
		// Generate the DataSet from Metric Data
		ChartData data = obtainChartDataFromMetric(metric, statistics);
		
		// Create the Chart
		JFreeChart chart = ChartFactory.createXYLineChart(
				data.getChartName(), "Time", data.getUnit(), data.getDataset(),
				PlotOrientation.VERTICAL,
	            true,                     // include legend
	            true,                     // tooltips
	            false                     // urls
	            );
		
		// Customise the Chart (optional)
        XYPlot plot = chart.getXYPlot();
        plot.setForegroundAlpha(0.5f);
        
		// Change the X-axis unit to String - Timestamp of each Datapoint
        if (data.getTimeAxis().size() > 0) {
        	String[] timeAxis =  new String[data.getTimeAxis().size()];
        	timeAxis = data.getTimeAxis().toArray(timeAxis);
	    
        	SymbolAxis domainAxis = new SymbolAxis("Time", timeAxis);

        	domainAxis.setTickUnit(new NumberTickUnit(1));
        	domainAxis.setRange(0, timeAxis.length);
        	plot.setDomainAxis(domainAxis);
        }
        
	    // Hide the X-axis tick labels
	    plot.getDomainAxis().setTickLabelsVisible(false);
	    
	    return chart;
	}

	/**
	 * Obtain all required Chart Data from a given Metric in order to plot a Line Chart
	 * 
	 * @param metric the Metric that we want to obtain Chart data
	 * @param statistics the list of Statistics (SUM, AVERAGE, etc.) we want to plot on the graph
	 * @return the ChartData containing all Chart Information
	 */
	private ChartData obtainChartDataFromMetric(Metric metric, List<String> statistics) {
		
		// There are 4 pieces of data for plotting a Chart
		String chartName;
		String unit;
		XYDataset dataset;
		List<String> timeAxis = new ArrayList<String>();
		
		/*** 1. Obtain the XYDatasets ***/
		
		// Create a Dataset with the Metric as a Data Series
		XYSeriesCollection dataCollection = new XYSeriesCollection();
		
		// Parse the Metric JSON data to a series of Datapoints
		Gson gson = new Gson();
		Type typeOfT = new TypeToken<List<Datapoint>>(){}.getType();	
		List<Datapoint> dps = gson.fromJson(metric.getJsonData(), typeOfT);
		
		// Date Parser to String format
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		if (statistics != null) {
		
			// Add data series for multiple aggregations (AVERAGE, SUM, MAX, MIN)
			// Should not be able to plot SAMPLECOUNT (since the unit is different)
			for (String stat : statistics) {
				if (stat.equals("Average")) {
					XYSeries s = new XYSeries("Average");
					for (int i = 0; i < dps.size(); i++) {
						s.add(i, dps.get(i).getAverage());
					}
					dataCollection.addSeries(s);
				} else if (stat.equals("Sum")) {
					XYSeries s = new XYSeries("Sum");
					for (int i = 0; i < dps.size(); i++) {
						s.add(i, dps.get(i).getSum());
					}
					dataCollection.addSeries(s);
				} else if (stat.equals("Maximum")) {
					XYSeries s = new XYSeries("Maximum");
					for (int i = 0; i < dps.size(); i++) {
						s.add(i, dps.get(i).getMaximum());
					}
					dataCollection.addSeries(s);
				} else if (stat.equals("Minimum")) {
					XYSeries s = new XYSeries("Minimum");
					for (int i = 0; i < dps.size(); i++) {
						s.add(i, dps.get(i).getMinimum());
					}
					dataCollection.addSeries(s);
				}
				// Simply ignore other statistics than the above 4
	 		}
		}
		
		dataset = (XYDataset) dataCollection;
		
		/*** 2. Obtain the TimeAxis string ***/
		for (int i = 0; i < dps.size(); i++) {
			timeAxis.add(sdf.format(dps.get(i).getTimestamp()));
		}
		
		/*** 3. Obtain the Metric Unit (in Percent, Bytes, etc.) ***/
		if (dps.size() > 0) {
			unit = dps.get(0).getUnit();
		} else {
			unit = "N/A";
		}
		
		/*** 4. Obtain the Chart Name, which is the Metric Name ***/
		chartName = metric.getName();
		
		// Return the bundled Data
		ChartData data = new ChartData(chartName, unit, dataset, timeAxis);
		return data;
	}
	
}
