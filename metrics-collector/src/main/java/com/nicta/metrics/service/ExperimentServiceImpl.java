package com.nicta.metrics.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nicta.metrics.dao.ExperimentRepository;
import com.nicta.metrics.domain.Experiment;
import com.nicta.metrics.exception.EntityDependenciesException;
import com.nicta.metrics.exception.ExperimentNotFoundException;

/**
 * Implements the operations related to Experiment
 * 
 * @author anbinhtran
 *
 */
@Service
public class ExperimentServiceImpl implements ExperimentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentServiceImpl.class);
    
    @Autowired
    private ExperimentRepository experimentRepository;
    
	@Override
	@Transactional
	public Experiment createCompleteExperiment(String asgName, String elbName,
			Long totalInstances, Long concurrentUpgrades, Date rollingStart,
			Date rollingEnd, Date dataCollectionStart, Date dataCollectionEnd) {
		LOGGER.debug("Create a complete Experiment entity for collecting data");
		
		Experiment experiment = new Experiment(
				asgName, elbName, totalInstances, concurrentUpgrades, 
				rollingStart, rollingEnd, dataCollectionStart, dataCollectionEnd);
		
		return experimentRepository.saveAndFlush(experiment);
	}

	@Override
	@Transactional
	public Experiment startExperiment(String asgName, String elbName,
			Long totalInstances, Long concurrentUpgrades) {
		LOGGER.debug("Create and Start a new Experiment");

		// Use current System time as Start time
		Date start = new Date();
		
		// If the Experiment involves a Rolling Upgrade activity,
		// also set the Rolling Upgrade StartTime
		Date rollingStart = null;
		if (concurrentUpgrades != null && concurrentUpgrades != 0) {
			rollingStart = start;
		}
		
		// Create an Experiment object with the Data Collection EndTime
		// (and Rolling Upgrade EndTime, if it involves Rolling Upgrade) to be null
		Experiment experiment = new Experiment(
				asgName, elbName, totalInstances, concurrentUpgrades, 
				rollingStart, null, start, null);
		
		return experimentRepository.saveAndFlush(experiment);
	}

	@Override
	@Transactional(rollbackFor = {ExperimentNotFoundException.class, IllegalArgumentException.class})
	public Experiment stopExperiment(Long id) 
			throws ExperimentNotFoundException, IllegalArgumentException {
		LOGGER.debug("Stop the Experiment with id: " + id);
		
		Experiment experiment = experimentRepository.findOne(id);
		
		if (experiment == null) {
			LOGGER.debug("No experiment found with id: " + id);
			throw new ExperimentNotFoundException();
		}
		
		// Only stop Experiment if it hasn't been stopped yet
		if (experiment.getDataCollectionEndTime() != null) {
			throw new IllegalArgumentException("Cannot stop an Experiment that was already stopped!");
		}
		
		// Use current System time as End time
		Date end = new Date();
		
		// Update the Data Collection EndTime
		experiment.setDataCollectionEndTime(end);
		// If the Experiment involves a Rolling Upgrade activity,
		// update the EndTime of Rolling Upgrade as well
		if (experiment.getRollingStartTime() != null) {
			experiment.setRollingEndTime(end);
		}
		
		return experiment;
	}

	@Override
	@Transactional(readOnly = true)
	public Experiment getExperimentById(Long id) {
		LOGGER.debug("Finding experiment by id: " + id);
		return experimentRepository.findOne(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Experiment> getAllExperiments() {
		LOGGER.debug("Finding all experiments");
		return experimentRepository.findAll();
	}

	@Override
	@Transactional(rollbackFor = {ExperimentNotFoundException.class, EntityDependenciesException.class})
	public Experiment deleteExperiment(Long id) 
			throws ExperimentNotFoundException, EntityDependenciesException {
		LOGGER.debug("Deleting experiment with id: " + id);
		
		Experiment deleted = experimentRepository.findOne(id);
		
		if (deleted == null) {
            LOGGER.debug("No experiment found with id: " + id);
            throw new ExperimentNotFoundException();
        }
        
		// Cannot delete Experiment if there are Metrics currently associated with it
		if (!deleted.getMetrics().isEmpty()) {
			throw new EntityDependenciesException();
		}
		
        experimentRepository.delete(deleted);
        return deleted;
	}

	@Override
	@Transactional(readOnly = true)
	public Long countExperiments() {
		return experimentRepository.count();
	}

	/**
	 * This setter method should be used only by unit tests
	 * 
	 * @param experimentRepository the ExperimentRepository to set
	 */
	public void setExperimentRepository(ExperimentRepository experimentRepository) {
		this.experimentRepository = experimentRepository;
	}
}
