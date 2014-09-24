package com.nicta.metrics.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nicta.metrics.service.aws.AwsCredentialsService;
import com.nicta.metrics.service.aws.AwsEndpointService;
import com.nicta.metrics.utility.aws.AwsRegions;

@Controller
@RequestMapping("/aws")
public class AwsController {

	private static final Logger logger = LoggerFactory.getLogger(AwsController.class);
	
	@Autowired
	AwsCredentialsService awsCredentialsService;
	
	@Autowired
	AwsEndpointService awsEndpointService;
	
	@RequestMapping(value = "region", method = RequestMethod.POST)
	public String setAwsRegion(@RequestParam String region, Model model) {
		
		// Create/Update AWS Region
		try {
			awsEndpointService.setAwsRegion(region);
		} catch (IllegalArgumentException e) {
			String message = "Invalid AWS Region specified!";
			logger.info(message);
			model.addAttribute("message", message);
			return "error";
		} catch (IOException e) {
			String message = "IOException occurred when try to access AWS Configuration file!";
			logger.info(message);
			model.addAttribute("message", message);
			return "error";
		}
		
		// Go to Home page
		return "redirect:/";
	}
	
	@RequestMapping(value = "credentials", method = RequestMethod.POST)
	public String setAwsCredentials(
			@RequestParam String awsAccessKeyId,
			@RequestParam String awsSecretAccessKey,
			@RequestParam String region,
			Model model) {
		
		// Create/Update AWS Credentials
		try {
			awsCredentialsService.createOrUpdateCredentials(awsAccessKeyId, awsSecretAccessKey);
		} catch (IllegalArgumentException e) {
			String message = "Invalid AWS Access Key and/or Secret Key specified!";
			logger.info(message);
			model.addAttribute("message", message);
			return "error";
		} catch (IOException e) {
			String message = "IOException occurred when try to access AWS Configuration file!";
			logger.info(message);
			model.addAttribute("message", message);
			return "error";
		}

		// Create/Update AWS Region
		try {
			awsEndpointService.setAwsRegion(region);
		} catch (IllegalArgumentException e) {
			String message = "Invalid AWS Region specified!";
			logger.info(message);
			model.addAttribute("message", message);
			return "error";
		} catch (IOException e) {
			String message = "IOException occurred when try to access AWS Configuration file!";
			logger.info(message);
			model.addAttribute("message", message);
			return "error";
		}

		// Go to Home page
		return "redirect:/";
	}
	
	@RequestMapping(value = "credentials", method = RequestMethod.GET)
	public String displayAwsSettingsPage(Model model) {
		AwsRegions[] regions = AwsRegions.values();
		model.addAttribute("regions", regions);
		return "aws_settings";
	}
}
