package com.nicta.metrics.controllertest;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nicta.metrics.config.JpaConfiguration;

@ContextConfiguration(classes = {JpaConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class MetricControllerTest {

	private static final String TYPE_CUSTOM = "custom";
	private static final String TYPE_AGGREGATED = "aggregated";
	private static final String NAME = "CPUUtilization";
	private static final String NAMESPACE = "AWS/EC2";
	private static final String DIMENSION_KEY = "InstanceId";
	private static final String DIMENSION_VALUE = "i-12345678";
	private static final String DIMENSIONS = "Name=InstanceId,Value=i-12345678";
	private static final long START_TIMESTAMP = Long.valueOf(1400112300000l);
	private static final long END_TIMESTAMP = Long.valueOf(1400115900000l);
	private static final Long PERIOD = Long.valueOf(60);
	
	private Date start;
	private Date end;
	
	@Before
	public void setUp() {
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
