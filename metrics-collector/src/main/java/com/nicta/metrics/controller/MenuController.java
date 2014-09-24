package com.nicta.metrics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nicta.metrics.domain.Statistics;
import com.nicta.metrics.service.ExperimentService;
import com.nicta.metrics.service.MetricService;
import com.nicta.metrics.service.aws.AwsEndpointService;

@Controller
@RequestMapping("/menu")
public class MenuController {
	
	@Autowired
	ExperimentService experimentService;
	
	@Autowired
	MetricService metricService;
	
	@Autowired
	AwsEndpointService awsEndpointService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String redirect(
			@ModelAttribute("statistics") Statistics statistics,
			@RequestParam String command) {
		if (command.equals("search")) {
			return "redirect:/";
		} else if (command.equals("collect")) {
			return "experiments/create";
		}
		return "redirect:/";
	}
	
	@ModelAttribute("statistics")
	public Statistics getDatabaseStatistics() {
		return new Statistics(
				awsEndpointService.getSelectedAwsRegion(), 
				experimentService.countExperiments(), 
				metricService.countMetrics());
	}
}
