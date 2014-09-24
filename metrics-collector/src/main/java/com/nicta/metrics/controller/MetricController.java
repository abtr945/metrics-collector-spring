package com.nicta.metrics.controller;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.ProviderException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.GenericValidator;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nicta.metrics.domain.Experiment;
import com.nicta.metrics.domain.Metric;
import com.nicta.metrics.domain.MetricJson;
import com.nicta.metrics.domain.Statistics;
import com.nicta.metrics.exception.BadParameterException;
import com.nicta.metrics.exception.InternalServerException;
import com.nicta.metrics.exception.MetricNotFoundException;
import com.nicta.metrics.service.ChartService;
import com.nicta.metrics.service.ExperimentService;
import com.nicta.metrics.service.MetricService;
import com.nicta.metrics.service.aws.AwsEndpointService;
import com.nicta.metrics.utility.profile.MetricProfile;
import com.nicta.metrics.utility.profile.MetricProfile_1_ASGandELB;
import com.nicta.metrics.utility.profile.MetricProfile_2_ASG;
import com.nicta.metrics.utility.profile.MetricProfile_3_ELB;

@Controller
@RequestMapping("metrics")
public class MetricController {
	
	private static final Long ALL_METRICS = -1l;

	private static final Logger logger = LoggerFactory.getLogger(MetricController.class);
	
	@Autowired
	ExperimentService experimentService;
	
	@Autowired
	MetricService metricService;
	
	@Autowired
	ChartService chartService;
	
	@Autowired
	AwsEndpointService awsEndpointService;
	
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public String show(
			@ModelAttribute("statistics") Statistics statistics, 
			@PathVariable Long id, Model model) throws MetricNotFoundException {
		Metric metric = metricService.getMetricById(id);
		if (metric == null) {
			logger.info("Cannot find Metric with id: " + id);
			throw new MetricNotFoundException();
		}
		
		model.addAttribute("metric", metric);
		return "metrics/show";	
	}
	
	@RequestMapping(value = "custom", method = RequestMethod.POST)
	public String createAndCollectCustomMetric(
			@RequestParam Long experimentId, 
			@RequestParam String metricName, @RequestParam String namespace,
			@RequestParam String dimensionKey, @RequestParam String dimensionValue, 
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm Z") Date start, 
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm Z") Date end, 
			@RequestParam Long period, Model model) 
					throws BadParameterException, InternalServerException {
		
		// Create a custom Metric object
		Long metricId = null;
		try {
			metricId = metricService.createNewCustomMetric(
					experimentId, metricName, namespace, 
					dimensionKey, dimensionValue, start, end, period).getId();
		} catch (IllegalArgumentException e) {
			logger.info(e.getMessage());
			throw new BadParameterException();
		}
		
		// Get the metric JSON_DATA from Amazon CloudWatch and update the object in persistence layer
		try {
			metricService.collectMetricDataFromCloudWatch(metricId);
		} catch (MetricNotFoundException e) {
			logger.info("Invalid Metric ID provided");
			throw new InternalServerException();
		} catch (IllegalArgumentException | ProviderException e) {
			logger.info(e.getMessage());
			throw new InternalServerException();
		}
		
		return "redirect:/experiments/" + experimentId;
	}
	
	@RequestMapping(value = "aggregated", method = RequestMethod.POST)
	public String collectAggregatedMetricsForExperiment(@RequestParam Long experimentId, Model model) 
			throws BadParameterException, InternalServerException {
		
		// Get the Experiment
		Experiment exp = experimentService.getExperimentById(experimentId);
		if (exp == null) {
			logger.info("Cannot find Experiment with id: " + experimentId);
			throw new BadParameterException();
		}
		
		// Get the correct Profile for collecting aggregated Metrics based on the Experiment type
		MetricProfile profile = null;
		boolean canCollect = true;
		
		if (!GenericValidator.isBlankOrNull(exp.getAsgName()) && 
				!GenericValidator.isBlankOrNull(exp.getElbName())) {
			// Experiment involves both ASG and ELB
			profile = new MetricProfile_1_ASGandELB();
		} else if (!GenericValidator.isBlankOrNull(exp.getAsgName())) {
			// Experiment involves only ASG
			profile = new MetricProfile_2_ASG();
		} else if (!GenericValidator.isBlankOrNull(exp.getElbName())) {
			// Experiment involves only ELB
			profile = new MetricProfile_3_ELB();
		} else {
			// Experiment involves neither ASG nor ELB, cannot collect any aggregated Metrics
			// => do nothing
			canCollect = false;
		}
		
		// Collect the aggregated Metrics specified in the Profile
		if (canCollect) {
			Map<String, String> metrics = profile.getMetricParams();
			
			Iterator<Map.Entry<String, String>> it = metrics.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
		        
		        // Create a Metric for each parameter set
		        Metric metric = null;
				try {
					metric = metricService.createNewMetricFromAutoScalingExperiment(
							exp.getId(), pairs.getKey(), pairs.getValue(), profile.getPeriod());
				} catch (IllegalArgumentException e) {
					logger.info(e.getMessage());
					throw new InternalServerException();
				}
				
				// Get the metric JSON_DATA from Amazon CloudWatch and update the object in persistence layer
				try {
					metricService.collectMetricDataFromCloudWatch(metric.getId());
				} catch (MetricNotFoundException e) {
					logger.info("Invalid Metric ID provided");
					throw new InternalServerException();
				} catch (IllegalArgumentException | ProviderException e) {
					logger.info(e.getMessage());
					throw new InternalServerException();
				}
		        
		        it.remove(); // avoids a ConcurrentModificationException
		    }
		}
		
		// Redirect to Display the updated Experiment with aggregated Metrics collected
		return "redirect:/experiments/" + experimentId;
	}
	
	@RequestMapping(value = "viewdata", method = RequestMethod.GET)
	@ResponseBody
	public String viewMetricJsonData(
			@RequestParam Long experimentId, @RequestParam Long metricId) 
					throws BadParameterException {
		
		Experiment exp = experimentService.getExperimentById(experimentId);
		if (exp == null) {
			logger.info("Cannot find Experiment with id: " + experimentId);
			throw new BadParameterException();
		}
		
		// View consolidated Metric data for the whole Experiment
		if (metricId == ALL_METRICS) {
			
			// List to store json formatted Metrics
			List<MetricJson> mjsons = new ArrayList<MetricJson>();
			
			// Json parser
			Gson gson = new Gson();
			Type typeOfT = new TypeToken<List<Datapoint>>(){}.getType();
			
			// Get the relevant metric data
			for (Metric m : exp.getMetrics()) {
				List<Datapoint> dps = gson.fromJson(m.getJsonData(), typeOfT);
				
				MetricJson mjson = new MetricJson(m.getName(), m.getDimensions(), dps);
				mjsons.add(mjson);
			}
			
			// Output the data
			return gson.toJson(mjsons);
		} 
		
		// Else, view JSON data for a single Metric
		else {
			
			// Get the Metric
			Metric m = metricService.getMetricById(metricId);
			if (m == null) {
				logger.info("Cannot find Metric with id: " + metricId);
				throw new BadParameterException();
			}
			
			// Json parser
			Gson gson = new Gson();
			Type typeOfT = new TypeToken<List<Datapoint>>(){}.getType();
			
			List<Datapoint> dps = gson.fromJson(m.getJsonData(), typeOfT);	
			MetricJson mjson = new MetricJson(m.getName(), m.getDimensions(), dps);
			
			// Output the data
			return gson.toJson(mjson);
		}
	}
	
	@RequestMapping(value = "download", method = RequestMethod.GET)
	public void downloadMetricJsonData(
			@RequestParam Long experimentId, @RequestParam Long metricId, HttpServletResponse response) 
					throws InternalServerException, BadParameterException {
		
		Experiment exp = experimentService.getExperimentById(experimentId);
		if (exp == null) {
			logger.info("Cannot find Experiment with id: " + experimentId);
			throw new BadParameterException();
		}
		
		// View consolidated Metric data for the whole Experiment
		if (metricId == ALL_METRICS) {
			
			// List to store json formatted Metrics
			List<MetricJson> mjsons = new ArrayList<MetricJson>();
			
			// Json parser
			Gson gson = new Gson();
			Type typeOfT = new TypeToken<List<Datapoint>>(){}.getType();
			
			// Get the relevant metric data
			for (Metric m : exp.getMetrics()) {
				List<Datapoint> dps = gson.fromJson(m.getJsonData(), typeOfT);
				
				MetricJson mjson = new MetricJson(m.getName(), m.getDimensions(), dps);
				mjsons.add(mjson);
			}
			
			// Write the data to HttpServletResponse for download
			try {
				ServletOutputStream outStream = response.getOutputStream();
			       
		        response.setContentType("application/octet-stream");
		        
		        // sets HTTP header
		        response.setHeader("Content-Disposition", "attachment; filename=\"" + "experiment" + exp.getId() + "_all.txt" + "\"");
		        
		        outStream.println(gson.toJson(mjsons));
		        outStream.close();
			} catch (IOException e) {
				logger.info("IOException occurred while preparing JSON data file for download");
				throw new InternalServerException();
			}
		} 
		
		// Else, view JSON data for a single Metric
		else {
			
			// Get the Metric
			Metric m = metricService.getMetricById(metricId);
			if (m == null) {
				logger.info("Cannot find Metric with id: " + metricId);
				throw new BadParameterException();
			}
			
			// Json parser
			Gson gson = new Gson();
			Type typeOfT = new TypeToken<List<Datapoint>>(){}.getType();
			
			List<Datapoint> dps = gson.fromJson(m.getJsonData(), typeOfT);	
			MetricJson mjson = new MetricJson(m.getName(), m.getDimensions(), dps);
			
			// Write the data to HttpServletResponse for download
			try {
				ServletOutputStream outStream = response.getOutputStream();
			       
		        response.setContentType("application/octet-stream");
		        
		        // sets HTTP header
		        response.setHeader("Content-Disposition", "attachment; filename=\"" + "experiment" + exp.getId() + "_" + m.getId() + ".txt" + "\"");
		        
		        outStream.println(gson.toJson(mjson));
		        outStream.close();
			} catch (IOException e) {
				logger.info("IOException occurred while preparing JSON data file for download");
				throw new InternalServerException();
			}
		}
	}
	
	@RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
	public String deleteMetric(
			@ModelAttribute("statistics") Statistics statistics, 
			@PathVariable Long id, Model model) throws MetricNotFoundException {
		Metric metric = metricService.deleteMetric(id);
		model.addAttribute("thingDeleted", "metric");
		model.addAttribute("deletedObject", metric);
		return "delete_confirmation";
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public void deleteMetricREST(@PathVariable Long id) throws MetricNotFoundException {
		metricService.deleteMetric(id);
	}
	
	@RequestMapping(value = "chart/{id}", method = RequestMethod.POST)
	public void getMetricChart(
			@PathVariable Long id, 
			@RequestParam(required = false) List<String> statistics,
			HttpServletResponse response) throws BadParameterException, IOException {
		
		// Get the Metric
		Metric m = metricService.getMetricById(id);
		if (m == null) {
			logger.info("Cannot find Metric with id: " + id);
			throw new BadParameterException();
		}
		
		response.setContentType("image/png");
		
		JFreeChart chart = chartService.createLineChart(m, statistics);
		
		ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 750, 400);
		
		response.getOutputStream().close();
	}
	
	@ModelAttribute("statistics")
	public Statistics getDatabaseStatistics() {
		return new Statistics(
				awsEndpointService.getSelectedAwsRegion(), 
				experimentService.countExperiments(), 
				metricService.countMetrics());
	}
}
