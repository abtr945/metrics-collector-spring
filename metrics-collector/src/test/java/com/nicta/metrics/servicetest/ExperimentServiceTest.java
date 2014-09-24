package com.nicta.metrics.servicetest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nicta.metrics.config.JpaConfiguration;
import com.nicta.metrics.dao.ExperimentRepository;
import com.nicta.metrics.domain.Experiment;
import com.nicta.metrics.domain.Metric;
import com.nicta.metrics.exception.EntityDependenciesException;
import com.nicta.metrics.exception.ExperimentNotFoundException;
import com.nicta.metrics.service.ExperimentServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(classes = {JpaConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class ExperimentServiceTest {
	
	private static final Long EXPERIMENT_ID = Long.valueOf(5);
	private static final String ASG_NAME = "testasg";
	private static final String ELB_NAME = "testelb";
	private static final Long TOTAL_INSTANCES = Long.valueOf(20);
	private static final Long CONCURRENT_UPGRADES = Long.valueOf(5);
	private static final long START_TIMESTAMP = Long.valueOf(1400112300000l);
	private static final long END_TIMESTAMP = Long.valueOf(1400115900000l);
	
	private ExperimentServiceImpl service;
	private ExperimentRepository experimentRepositoryMock;
	
	private Date start;
	private Date end;
	
	@Before
	public void setUp() {
		service = new ExperimentServiceImpl();
		experimentRepositoryMock = mock(ExperimentRepository.class);
		
		service.setExperimentRepository(experimentRepositoryMock);
		
		start = new Date(START_TIMESTAMP);
		end = new Date(END_TIMESTAMP);
    }
	
	@Test
	public void create() {
		// Set up mock object behaviour
		Experiment persisted = new Experiment(
				ASG_NAME, ELB_NAME, TOTAL_INSTANCES, CONCURRENT_UPGRADES, 
				start, end, start, end);
		persisted.setId(EXPERIMENT_ID);
		when(experimentRepositoryMock.saveAndFlush(any(Experiment.class))).thenReturn(persisted);
		
		// Execute the operation
		Experiment returned = service.createCompleteExperiment(
				ASG_NAME, ELB_NAME, TOTAL_INSTANCES, CONCURRENT_UPGRADES, 
				start, end, start, end);
		
		// Verify number of mock function calls
		ArgumentCaptor<Experiment> experimentArgument = ArgumentCaptor.forClass(Experiment.class);
		verify(experimentRepositoryMock, times(1)).saveAndFlush(experimentArgument.capture());
		verifyNoMoreInteractions(experimentRepositoryMock);
		
		// Verify mock function arguments
		assertSameExperimentParams(persisted, experimentArgument.getValue());
		
		// Verify operation return value
		assertEquals(persisted, returned);
	}
	
	@Test
	public void startRolling() {
		// Execute the action
		service.startExperiment(ASG_NAME, ELB_NAME, TOTAL_INSTANCES, CONCURRENT_UPGRADES);
		
		// Verify number of mock function calls
		ArgumentCaptor<Experiment> experimentArgument = ArgumentCaptor.forClass(Experiment.class);
		verify(experimentRepositoryMock, times(1)).saveAndFlush(experimentArgument.capture());
		verifyNoMoreInteractions(experimentRepositoryMock);
		
		// Verify mock function arguments
		assertEquals(ASG_NAME, experimentArgument.getValue().getAsgName());
		assertEquals(ELB_NAME, experimentArgument.getValue().getElbName());
		assertEquals(TOTAL_INSTANCES, experimentArgument.getValue().getTotalInstances());
		assertEquals(CONCURRENT_UPGRADES, experimentArgument.getValue().getConcurrentUpgrades());
		
		// DataCollection start time should be current system time (i.e. not null)
		assertNotNull(experimentArgument.getValue().getDataCollectionStartTime());
		// Since this is a Rolling Upgrade experiment, 
		// RollingStartTime should also be current system time (i.e. not null)
		assertNotNull(experimentArgument.getValue().getRollingStartTime());
		
		// The Experiment is ongoing, so both Rolling and Data Collection End time should be null
		assertNull(experimentArgument.getValue().getRollingEndTime());
		assertNull(experimentArgument.getValue().getDataCollectionEndTime());
	}
	
	@Test
	public void startNoRollingConcurrentUpgradesNull() {
		// Set up mock object behaviour
		Long concurrentUpgradesNoRolling = null;
		
		// Execute the action
		service.startExperiment(ASG_NAME, ELB_NAME, TOTAL_INSTANCES, concurrentUpgradesNoRolling);
		
		// Verify number of mock function calls
		ArgumentCaptor<Experiment> experimentArgument = ArgumentCaptor.forClass(Experiment.class);
		verify(experimentRepositoryMock, times(1)).saveAndFlush(experimentArgument.capture());
		verifyNoMoreInteractions(experimentRepositoryMock);
		
		// Verify mock function arguments
		assertEquals(ASG_NAME, experimentArgument.getValue().getAsgName());
		assertEquals(ELB_NAME, experimentArgument.getValue().getElbName());
		assertEquals(TOTAL_INSTANCES, experimentArgument.getValue().getTotalInstances());
		assertEquals(concurrentUpgradesNoRolling, experimentArgument.getValue().getConcurrentUpgrades());
		
		// Since this is not a Rolling Upgrade, RollingStartTime should be null
		assertNull(experimentArgument.getValue().getRollingStartTime());
		// DataCollection start time should be current system time (i.e. not null)
		assertNotNull(experimentArgument.getValue().getDataCollectionStartTime());
		
		// The Experiment is ongoing, so both Rolling and Data Collection End time should be null
		assertNull(experimentArgument.getValue().getRollingEndTime());
		assertNull(experimentArgument.getValue().getDataCollectionEndTime());
	}
	
	@Test
	public void startNoRollingConcurrentUpgradesZero() {
		// Set up mock object behaviour
		Long concurrentUpgradesNoRolling = Long.valueOf(0);
		
		// Execute the action
		service.startExperiment(ASG_NAME, ELB_NAME, TOTAL_INSTANCES, concurrentUpgradesNoRolling);
		
		// Verify number of mock function calls
		ArgumentCaptor<Experiment> experimentArgument = ArgumentCaptor.forClass(Experiment.class);
		verify(experimentRepositoryMock, times(1)).saveAndFlush(experimentArgument.capture());
		verifyNoMoreInteractions(experimentRepositoryMock);
		
		// Verify mock function arguments
		assertEquals(ASG_NAME, experimentArgument.getValue().getAsgName());
		assertEquals(ELB_NAME, experimentArgument.getValue().getElbName());
		assertEquals(TOTAL_INSTANCES, experimentArgument.getValue().getTotalInstances());
		assertEquals(concurrentUpgradesNoRolling, experimentArgument.getValue().getConcurrentUpgrades());
		
		// Since this is not a Rolling Upgrade, RollingStartTime should be null
		assertNull(experimentArgument.getValue().getRollingStartTime());
		// DataCollection start time should be current system time (i.e. not null)
		assertNotNull(experimentArgument.getValue().getDataCollectionStartTime());
		
		// The Experiment is ongoing, so both Rolling and Data Collection End time should be null
		assertNull(experimentArgument.getValue().getRollingEndTime());
		assertNull(experimentArgument.getValue().getDataCollectionEndTime());
	}
	
	@Test
	public void stopRolling() throws ExperimentNotFoundException, IllegalArgumentException {
		// Set up mock object behaviour
		Experiment runningExp = new Experiment(
				ASG_NAME, ELB_NAME, TOTAL_INSTANCES, CONCURRENT_UPGRADES, 
				start, null, start, null);
		runningExp.setId(EXPERIMENT_ID);
		
		when(experimentRepositoryMock.findOne(EXPERIMENT_ID)).thenReturn(runningExp);
		
		// Execute the operation
		Experiment returned = service.stopExperiment(EXPERIMENT_ID);
		
		// Verify the number of mock function calls
		verify(experimentRepositoryMock, times(1)).findOne(EXPERIMENT_ID);
		verifyNoMoreInteractions(experimentRepositoryMock);
		
		/* Verify the operation return value */
		// These parameters should be the same as the original Experiment
		assertEquals(runningExp.getAsgName(), returned.getAsgName());
		assertEquals(runningExp.getElbName(), returned.getElbName());
		assertEquals(runningExp.getTotalInstances(), returned.getTotalInstances());
		assertEquals(runningExp.getConcurrentUpgrades(), returned.getConcurrentUpgrades());
		assertEquals(runningExp.getDataCollectionStartTime(), returned.getDataCollectionStartTime());
		assertEquals(runningExp.getRollingStartTime(), returned.getRollingStartTime());
		
		// The DataCollection End time should now be set to current system time (i.e. not null anymore)
		assertNotNull(returned.getDataCollectionEndTime());
		// This is a Rolling Upgrade experiment, thus the RollingEndTime should also be set to
		// current system time (i.e. not null)
		assertNotNull(returned.getRollingEndTime());
	}
	
	@Test
	public void stopNoRolling() throws ExperimentNotFoundException, IllegalArgumentException {
		// Set up mock object behaviour
		Experiment runningExpNoRolling = new Experiment(
				ASG_NAME, ELB_NAME, TOTAL_INSTANCES, null, 
				null, null, start, null);
		runningExpNoRolling.setId(EXPERIMENT_ID);
		
		when(experimentRepositoryMock.findOne(EXPERIMENT_ID)).thenReturn(runningExpNoRolling);
		
		// Execute the operation
		Experiment returned = service.stopExperiment(EXPERIMENT_ID);
		
		// Verify the number of mock function calls
		verify(experimentRepositoryMock, times(1)).findOne(EXPERIMENT_ID);
		verifyNoMoreInteractions(experimentRepositoryMock);
		
		/* Verify the operation return value */
		// These parameters should be the same as the original Experiment
		assertEquals(runningExpNoRolling.getAsgName(), returned.getAsgName());
		assertEquals(runningExpNoRolling.getElbName(), returned.getElbName());
		assertEquals(runningExpNoRolling.getTotalInstances(), returned.getTotalInstances());
		assertEquals(runningExpNoRolling.getConcurrentUpgrades(), returned.getConcurrentUpgrades());
		assertEquals(
				runningExpNoRolling.getDataCollectionStartTime(), returned.getDataCollectionStartTime());
		assertEquals(runningExpNoRolling.getRollingStartTime(), returned.getRollingStartTime());
		
		// The DataCollection End time should now be set to current system time (i.e. not null anymore)
		assertNotNull(returned.getDataCollectionEndTime());
		// This is not a Rolling Upgrade experiment, thus the RollingEndTime will still be null
		assertNull(returned.getRollingEndTime());
	}
	
	@Test(expected = ExperimentNotFoundException.class)
	public void stopWhenExperimentIsNotFound() 
			throws ExperimentNotFoundException, IllegalArgumentException {
		// Set up mock object behaviour
		when(experimentRepositoryMock.findOne(EXPERIMENT_ID)).thenReturn(null);
		
		// Execute the operation
		service.stopExperiment(EXPERIMENT_ID);
		
		// Verify the number of mock function calls
		verify(experimentRepositoryMock, times(1)).findOne(EXPERIMENT_ID);
		verifyNoMoreInteractions(experimentRepositoryMock);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void stopWhenExperimentHasAlreadyStopped() 
			throws ExperimentNotFoundException, IllegalArgumentException {
		// Set up mock object behaviour
		Experiment expAlreadyStopped = new Experiment();
		expAlreadyStopped.setId(EXPERIMENT_ID);
		expAlreadyStopped.setDataCollectionEndTime(end);
		
		when(experimentRepositoryMock.findOne(EXPERIMENT_ID)).thenReturn(expAlreadyStopped);
		
		// Execute the operation
		service.stopExperiment(EXPERIMENT_ID);
		
		// Verify the number of mock function calls
		verify(experimentRepositoryMock, times(1)).findOne(EXPERIMENT_ID);
		verifyNoMoreInteractions(experimentRepositoryMock);
	}
	
	@Test
    public void delete() throws ExperimentNotFoundException, EntityDependenciesException {
		// Set up mock object behaviour
		Experiment deleted = new Experiment(
				ASG_NAME, ELB_NAME, TOTAL_INSTANCES, CONCURRENT_UPGRADES, 
				start, end, start, end);
		when(experimentRepositoryMock.findOne(EXPERIMENT_ID)).thenReturn(deleted);
		
		// Execute the operation
		Experiment returned = service.deleteExperiment(EXPERIMENT_ID);
		
		// Verify number of mock function calls
		verify(experimentRepositoryMock, times(1)).findOne(EXPERIMENT_ID);
		verify(experimentRepositoryMock, times(1)).delete(deleted);
		verifyNoMoreInteractions(experimentRepositoryMock);
		
		// Verify operation return value
		assertEquals(deleted, returned);
    }
	
	@Test(expected = ExperimentNotFoundException.class)
    public void deleteWhenExperimentIsNotFound() throws ExperimentNotFoundException, EntityDependenciesException {
		// Set up mock object behaviour
		when(experimentRepositoryMock.findOne(EXPERIMENT_ID)).thenReturn(null);
		
		// Execute the operation
        service.deleteExperiment(EXPERIMENT_ID);
        
        // Verify number of mock function calls
        verify(experimentRepositoryMock, times(1)).findOne(EXPERIMENT_ID);
        verifyNoMoreInteractions(experimentRepositoryMock);
    }
	
	@Test(expected = EntityDependenciesException.class)
	public void deleteWhenExperimentHasMetrics() throws ExperimentNotFoundException, EntityDependenciesException {
		// Set up mock object behaviour
		List<Metric> someMetrics = new ArrayList<Metric>();
		someMetrics.add(new Metric());
		Experiment expWithMetrics = new Experiment();
		expWithMetrics.setId(EXPERIMENT_ID);
		expWithMetrics.setMetrics(someMetrics);
		
		when(experimentRepositoryMock.findOne(EXPERIMENT_ID)).thenReturn(expWithMetrics);
		
		// Execute the operation
        service.deleteExperiment(EXPERIMENT_ID);
        
        // Verify number of mock function calls
        verify(experimentRepositoryMock, times(1)).findOne(EXPERIMENT_ID);
        verifyNoMoreInteractions(experimentRepositoryMock);
    }
	
	@Test
	public void findById() {
		// Set up mock object behaviour
		Experiment exp = new Experiment(
				ASG_NAME, ELB_NAME, TOTAL_INSTANCES, CONCURRENT_UPGRADES, 
				start, end, start, end);
		exp.setId(EXPERIMENT_ID);
		
		when(experimentRepositoryMock.findOne(EXPERIMENT_ID)).thenReturn(exp);
		
		// Execute the operation
		Experiment returned = service.getExperimentById(EXPERIMENT_ID);
		
		// Verify the number of mock function calls
		verify(experimentRepositoryMock, times(1)).findOne(EXPERIMENT_ID);
		verifyNoMoreInteractions(experimentRepositoryMock);
		
		// Verify the operation return value
		assertEquals(exp, returned);
	}
	
	@Test
	public void findAll() {
		// Set up mock object behaviour
		List<Experiment> exps = new ArrayList<Experiment>();
		
		when(experimentRepositoryMock.findAll()).thenReturn(exps);
		
		// Execute the operation
		List<Experiment> returned = service.getAllExperiments();
		
		// Verify the number of mock function calls
		verify(experimentRepositoryMock, times(1)).findAll();
		verifyNoMoreInteractions(experimentRepositoryMock);
		
		// Verify the operation return value
		assertEquals(exps, returned);
	}
	
	private void assertSameExperimentParams(Experiment expected, Experiment actual) {
		assertEquals(expected.getAsgName(), actual.getAsgName());
		assertEquals(expected.getElbName(), actual.getElbName());
		assertEquals(expected.getTotalInstances(), actual.getTotalInstances());
		assertEquals(expected.getConcurrentUpgrades(), actual.getConcurrentUpgrades());
		assertEquals(expected.getDataCollectionStartTime(), actual.getDataCollectionStartTime());
		assertEquals(expected.getDataCollectionEndTime(), actual.getDataCollectionEndTime());
		assertEquals(expected.getRollingStartTime(), actual.getRollingStartTime());
		assertEquals(expected.getRollingEndTime(), actual.getRollingEndTime());
	}
}
