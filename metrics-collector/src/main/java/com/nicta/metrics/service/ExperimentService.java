package com.nicta.metrics.service;

import java.util.Date;
import java.util.List;

import com.nicta.metrics.domain.Experiment;
import com.nicta.metrics.exception.EntityDependenciesException;
import com.nicta.metrics.exception.ExperimentNotFoundException;

/**
 * Define various operations on Experiment (create, find, update and delete)
 * 
 * @author anbinhtran
 *
 */
public interface ExperimentService {

	/**
	 * Create a complete Experiment entity - ready to have metrics collected
	 * 
	 * @param asgName null if experiment does not involve an Auto Scaling Group
	 * @param elbName null if experiment does not involve a Load Balancer
	 * @param totalInstances null if experiment does not involve an Auto Scaling Group
	 * @param concurrentUpgrades null if experiment does not involve a Rolling Upgrade activity
	 * @param rollingStart start time of Rolling Upgrade, null if no Rolling Upgrade
	 * @param rollingEnd end time of Rolling Upgrade, null if no Rolling Upgrade
	 * @param dataCollectionStart time to start collecting metrics data
	 * @param dataCollectionEnd time to end collecting metrics data
	 * @return the completed Experiment entity
	 */
	public Experiment createCompleteExperiment(
			String asgName, String elbName, Long totalInstances, Long concurrentUpgrades, 
			Date rollingStart, Date rollingEnd, Date dataCollectionStart, Date dataCollectionEnd);
		
	/**
	 * Create and start an Experiment. 
	 * 
	 * Use System current time as the Start time of the Experiment.
	 * 
	 * If the experiment involves a Rolling Upgrade activity,
	 * assume that the start time of Rolling Upgrade is also when we start collecting metrics data
	 * 
	 * @param asgName null if experiment does not involve an Auto Scaling Group
	 * @param elbName null if experiment does not involve a Load Balancer
	 * @param totalInstances null if experiment does not involve an Auto Scaling Group
	 * @param concurrentUpgrades null if experiment does not involve a Rolling Upgrade activity
	 * @return the created Experiment entity with null EndTime (i.e. not yet finished)
	 */
	public Experiment startExperiment(
			String asgName, String elbName, Long totalInstances, Long concurrentUpgrades);
	
	/**
	 * Stop an Experiment. 
	 * 
	 * Update the Experiment End time with the current System time.
	 * 
	 * @param id unique ID of experiment
	 * @return the updated Experiment entity
	 * @throws ExperimentNotFoundException if an Experiment with the specified ID cannot be found
	 * @throws IllegalArgumentException if trying to stop an Experiment that was already stopped
	 */
	public Experiment stopExperiment(Long id) 
			throws ExperimentNotFoundException, IllegalArgumentException;
	
	/**
	 * Get an Experiment by its ID
	 * 
	 * @param id unique ID of experiment
	 * @return the Experiment entity with the specified ID
	 */
	public Experiment getExperimentById(Long id);
	
	/**
	 * Get a list of all Experiments in database
	 * 
	 * @return a List containing all stored Experiment entities
	 */
	public List<Experiment> getAllExperiments();
	
	/**
	 * Delete an Experiment from Database.
	 * Can only delete if there is no Metric associated with that Experiment
	 * 
	 * @param id unique ID of experiment
	 * @return the deleted Experiment entity
	 * @throws ExperimentNotFoundException if an Experiment with the specified ID cannot be found
	 * @throws EntityDependenciesException if the deletion would violate dependency with Metric table
	 */
	public Experiment deleteExperiment(Long id) 
			throws ExperimentNotFoundException, EntityDependenciesException;
	
	/**
	 * Get the total number of Experiments recorded in Database
	 * @return the total number of Experiments
	 */
	public Long countExperiments();
	
}
