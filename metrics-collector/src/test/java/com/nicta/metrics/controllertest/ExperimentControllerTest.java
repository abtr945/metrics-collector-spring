package com.nicta.metrics.controllertest;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nicta.metrics.config.JpaConfiguration;
import com.nicta.metrics.controller.ExperimentController;
import com.nicta.metrics.domain.Experiment;
import com.nicta.metrics.exception.EntityDependenciesException;
import com.nicta.metrics.exception.ExperimentNotFoundException;
import com.nicta.metrics.service.ExperimentService;

@ContextConfiguration(classes = {JpaConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class ExperimentControllerTest {

	private static final String ASG_NAME = "testasg";
	private static final String ELB_NAME = "testelb";
	private static final Long TOTAL_INSTANCES = Long.valueOf(20);
	private static final Long CONCURRENT_UPGRADES = Long.valueOf(5);
	private static final Long START_TIMESTAMP = Long.valueOf(1400112300000l);
	private static final Long END_TIMESTAMP = Long.valueOf(1400115900000l);
	
	private Date start;
	private Date end;
	
	private static Long EXPERIMENT_ID;
	
	ExperimentController controller;
	@Autowired
	ExperimentService service;
	
	@Before
	public void setUp() {
		controller = new ExperimentController();
		start = new Date(START_TIMESTAMP);
		end = new Date(END_TIMESTAMP);
    }
	
	/*
	@Test
	public void createCompleteExperiment() {
		Experiment exp = controller.createExperimentREST(
				ASG_NAME, ELB_NAME, TOTAL_INSTANCES, CONCURRENT_UPGRADES, 
				START_TIMESTAMP, END_TIMESTAMP, START_TIMESTAMP, END_TIMESTAMP);
		
		EXPERIMENT_ID = exp.getId();
		
		assertEquals(service.countExperiments(), Long.valueOf(1));
		
		assertEquals(exp.getAsgName(), ASG_NAME);
		assertEquals(exp.getElbName(), ELB_NAME);
		assertEquals(exp.getTotalInstances(), TOTAL_INSTANCES);
		assertEquals(exp.getConcurrentUpgrades(), CONCURRENT_UPGRADES);
		assertEquals(exp.getRollingStartTime(), START_TIMESTAMP);
		assertEquals(exp.getRollingEndTime(), END_TIMESTAMP);
		assertEquals(exp.getDataCollectionStartTime(), START_TIMESTAMP);
		assertEquals(exp.getDataCollectionEndTime(), END_TIMESTAMP);
	}
	
	@Test
	public void deleteExperiment() throws ExperimentNotFoundException, EntityDependenciesException {
		controller.deleteExperimentREST(EXPERIMENT_ID);
		assertEquals(service.countExperiments(), Long.valueOf(0));
	}
	*/
	
	@Test
	public void mock() {
		assertEquals(Long.valueOf(0), Long.valueOf(0));
	}
}
