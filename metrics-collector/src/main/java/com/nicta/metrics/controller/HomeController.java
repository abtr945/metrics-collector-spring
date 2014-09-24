package com.nicta.metrics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nicta.metrics.domain.Statistics;
import com.nicta.metrics.service.ExperimentService;
import com.nicta.metrics.service.MetricService;
import com.nicta.metrics.service.aws.AwsEndpointService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Autowired
	ExperimentService experimentService;
	
	@Autowired
	MetricService metricService;
	
	@Autowired
	AwsEndpointService awsEndpointService;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(@ModelAttribute("statistics") Statistics statistics) {
		return "home";
	}
	
	@ModelAttribute("statistics")
	public Statistics getDatabaseStatistics() {
		return new Statistics(
				awsEndpointService.getSelectedAwsRegion(), 
				experimentService.countExperiments(), 
				metricService.countMetrics());
	}
}
