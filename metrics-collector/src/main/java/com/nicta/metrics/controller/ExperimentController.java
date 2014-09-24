package com.nicta.metrics.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.validator.GenericValidator;
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

import com.nicta.metrics.domain.Experiment;
import com.nicta.metrics.domain.Experiments;
import com.nicta.metrics.domain.Metric;
import com.nicta.metrics.domain.Statistics;
import com.nicta.metrics.exception.BadParameterException;
import com.nicta.metrics.exception.EntityDependenciesException;
import com.nicta.metrics.exception.ExperimentNotFoundException;
import com.nicta.metrics.service.ExperimentService;
import com.nicta.metrics.service.MetricService;
import com.nicta.metrics.service.aws.AwsEndpointService;
import com.nicta.metrics.utility.aws.MetricNamespace;

@Controller
@RequestMapping("/experiments")
public class ExperimentController {

	private static final Logger logger = LoggerFactory.getLogger(ExperimentController.class);
	
	@Autowired
	ExperimentService experimentService;
	
	@Autowired
	MetricService metricService;
	
	@Autowired
	AwsEndpointService awsEndpointService;
	
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public String show(
			@ModelAttribute("statistics") Statistics statistics, 
			@PathVariable Long id, Model model) throws ExperimentNotFoundException {
		Experiment exp = experimentService.getExperimentById(id);
		if (exp == null) {
			logger.info("Cannot find Experiment with id: " + id);
			throw new ExperimentNotFoundException();
		}
		
		// Calculate the number of Aggregated and Custom metrics associated with this Experiment
		int noAggregatedMetrics = 0;
		int noCustomMetrics = 0;
		for (Metric m : exp.getMetrics()) {
			if (m.getType().equals("aggregated")) {
				noAggregatedMetrics++;
			} else {
				noCustomMetrics++;
			}
		}
		
		model.addAttribute("experiment", exp);
		model.addAttribute("noAggregatedMetrics", noAggregatedMetrics);
		model.addAttribute("noCustomMetrics", noCustomMetrics);
		
		// Add an Enum of AWS Namespaces to the Model
		model.addAttribute("namespaces", MetricNamespace.values());
		
		return "experiments/show";
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String listAll(@ModelAttribute("statistics") Statistics statistics, Model model) {
		List<Experiment> exps = experimentService.getAllExperiments();
		model.addAttribute("experiments", exps);
		return "experiments/list";
	}
	
	@RequestMapping(value = "search", method = RequestMethod.POST)
	public String search(
			@ModelAttribute("statistics") Statistics statistics, 
			@RequestParam String searchType,
			@RequestParam(required = false) Long experimentId,
			@RequestParam(required = false) String asgName,
			@RequestParam(required = false) String elbName,
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date date,
			Model model) {
		// Quick Search
		if (searchType.equals("Quick Search")) {
			if (experimentId == null) {
				model.addAttribute("message", "No Experiment found matching search criteria!");
				return "error";
			}
			Experiment experiment = experimentService.getExperimentById(experimentId);
			if (experiment == null) {
				model.addAttribute("message", "No Experiment found matching search criteria!");
				return "error";
			}
			
			return "redirect:/experiments/" + experiment.getId();
		} 
		// Advanced Search
		else {
			// Don't need to do anything if nothing are entered
			if (GenericValidator.isBlankOrNull(asgName) && 
					GenericValidator.isBlankOrNull(elbName) && 
					(date == null)) {
				model.addAttribute("message", "No Experiment found matching search criteria!");
				return "error";
			}
			
			Date endBoundaryDate = null;
			if (date != null) {
				Calendar c = Calendar.getInstance(); 
				c.setTime(date); 
				c.add(Calendar.DATE, 1);
				endBoundaryDate = c.getTime();
			}
			
			// Get all Experiments
			List<Experiment> experiments = experimentService.getAllExperiments();
			// Filter the List with each search Param
			if (!GenericValidator.isBlankOrNull(asgName)) {
				Iterator<Experiment> it = experiments.iterator();
				while(it.hasNext()) {
					if (!it.next().getAsgName().equals(asgName)) {
						it.remove();
					}
				}
			}
			if (!GenericValidator.isBlankOrNull(elbName)) {
				Iterator<Experiment> it = experiments.iterator();
				while(it.hasNext()) {
					if (!it.next().getElbName().equals(elbName)) {
						it.remove();
					}
				}
			}
			if (date != null && endBoundaryDate != null) {
				Iterator<Experiment> it = experiments.iterator();
				while(it.hasNext()) {
					Experiment e = it.next();
					if (!((e.getDataCollectionStartTime().after(date) && e.getDataCollectionStartTime().before(endBoundaryDate)) ||
							(e.getDataCollectionEndTime().after(date) && e.getDataCollectionEndTime().before(endBoundaryDate)))) {
						it.remove();
					}
				}
			}
			
			// Return if Experiments is not empty
			if (experiments.isEmpty()) {
				model.addAttribute("message", "No Experiment found matching search criteria!");
				return "error";
			}
			
			Experiments marshallableExperimentList = new Experiments(experiments);
			model.addAttribute("experiments", marshallableExperimentList);
			return "experiments/list";
		}
	}
	
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String createExperiment(
			@RequestParam(required = false) String asgName, 
			@RequestParam(required = false) String elbName,
			@RequestParam(required = false) Long totalInstances, 
			@RequestParam(required = false) Long concurrentUpgrades,
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm Z") Date rollingStart, 
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm Z") Date rollingEnd,
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm Z") Date dataCollectionStart, 
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm Z") Date dataCollectionEnd) {

		Long experimentId = experimentService.createCompleteExperiment(
				asgName, elbName, totalInstances, concurrentUpgrades, 
				rollingStart, rollingEnd, dataCollectionStart, dataCollectionEnd).getId();
		
		return "redirect:/experiments/" + experimentId;
	}
	
	@RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
	public String deleteExperiment(
			@ModelAttribute("statistics") Statistics statistics, 
			@PathVariable Long id, Model model) 
					throws ExperimentNotFoundException, EntityDependenciesException {
		Experiment experiment = experimentService.deleteExperiment(id);
		model.addAttribute("thingDeleted", "experiment");
		model.addAttribute("deletedObject", experiment);
		return "delete_confirmation";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Experiment createExperimentREST(
			@RequestParam(required = false) String asgName, 
			@RequestParam(required = false) String elbName,
			@RequestParam(required = false) Long totalInstances, 
			@RequestParam(required = false) Long concurrentUpgrades,
			@RequestParam(required = false) Long rollingStartTimestamp, 
			@RequestParam(required = false) Long rollingEndTimestamp,
			@RequestParam Long dataCollectionStartTimestamp, 
			@RequestParam Long dataCollectionEndTimestamp) {
		
		// Parse the Timestamps to java Date
		Date dataCollectionStart = new Date(dataCollectionStartTimestamp);
		Date dataCollectionEnd = new Date(dataCollectionEndTimestamp);
		Date rollingStart = null;
		Date rollingEnd = null;
		if (rollingStartTimestamp != null) {
			rollingStart = new Date(rollingStartTimestamp);
		}
		if (rollingEndTimestamp != null) {
			rollingEnd = new Date(rollingEndTimestamp);
		}
		
		// Create new Experiment
		Experiment exp = experimentService.createCompleteExperiment(
				asgName, elbName, totalInstances, concurrentUpgrades, 
				rollingStart, rollingEnd, dataCollectionStart, dataCollectionEnd);
		
		// Return the Experiment
		return exp;
	}
	
	@RequestMapping(value = "start", method = RequestMethod.POST)
	@ResponseBody
	public String startNewExperimentREST(
			@RequestParam(required = false) String asgName, 
			@RequestParam(required = false) String elbName,
			@RequestParam(required = false) Long totalInstances, 
			@RequestParam(required = false) Long concurrentUpgrades) {
		Experiment exp = experimentService.startExperiment(
				asgName, elbName, totalInstances, concurrentUpgrades);
		return exp.getId().toString();
	}
	
	@RequestMapping(value = "stop/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public String stopExperimentREST(@PathVariable Long id) 
			throws ExperimentNotFoundException, BadParameterException {
		Experiment exp = null;
		try {
			exp = experimentService.stopExperiment(id);
		} catch (IllegalArgumentException e) {
			logger.info(e.getMessage());
			throw new BadParameterException();
		}
		return exp.getId().toString();
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public void deleteExperimentREST(@PathVariable Long id) 
			throws ExperimentNotFoundException, EntityDependenciesException {
		experimentService.deleteExperiment(id);
	}
	
	@ModelAttribute("statistics")
	public Statistics getDatabaseStatistics() {
		return new Statistics(
				awsEndpointService.getSelectedAwsRegion(), 
				experimentService.countExperiments(), 
				metricService.countMetrics());
	}
	
}
