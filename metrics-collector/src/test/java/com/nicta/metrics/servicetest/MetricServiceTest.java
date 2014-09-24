package com.nicta.metrics.servicetest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.mockito.ArgumentCaptor;

import java.security.ProviderException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nicta.metrics.config.JpaConfiguration;
import com.nicta.metrics.dao.ExperimentRepository;
import com.nicta.metrics.dao.MetricRepository;
import com.nicta.metrics.domain.Experiment;
import com.nicta.metrics.domain.Metric;
import com.nicta.metrics.exception.MetricNotFoundException;
import com.nicta.metrics.service.MetricServiceImpl;
import com.nicta.metrics.service.aws.AwsCloudWatchService;

@ContextConfiguration(classes = {JpaConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class MetricServiceTest {

	private static final Long EXPERIMENT_ID = Long.valueOf(2);
	private static final Long METRIC_ID = Long.valueOf(5);
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
	
	private MetricServiceImpl service;
	
	private MetricRepository metricRepositoryMock;
	private ExperimentRepository experimentRepositoryMock;
	private AwsCloudWatchService awsCloudWatchServiceMock;
	
	private Date start;
	private Date end;
	
	@Before
	public void setUp() {
		service = new MetricServiceImpl();
		metricRepositoryMock = mock(MetricRepository.class);
		experimentRepositoryMock = mock(ExperimentRepository.class);
		awsCloudWatchServiceMock = mock(AwsCloudWatchService.class);
		
		service.setMetricRepository(metricRepositoryMock);
		service.setExperimentRepository(experimentRepositoryMock);
		service.setAwsCloudWatchService(awsCloudWatchServiceMock);
		
		start = new Date(START_TIMESTAMP);
		end = new Date(END_TIMESTAMP);
    }
	
	@Test
	public void createCustom() throws IllegalArgumentException {
		// Set up mock object behaviour
		Metric persisted = new Metric(TYPE_CUSTOM, NAME, NAMESPACE, DIMENSIONS, start, end, PERIOD);
		persisted.setId(METRIC_ID);
		
		Experiment associatedExp = new Experiment();
		associatedExp.setId(EXPERIMENT_ID);
		associatedExp.addMetrics(persisted);
		
		when(experimentRepositoryMock.findOne(EXPERIMENT_ID)).thenReturn(associatedExp);
		when(metricRepositoryMock.saveAndFlush(any(Metric.class))).thenReturn(persisted);
		
		// Execute the operation
		Metric returned = service.createNewCustomMetric(
				EXPERIMENT_ID, NAME, NAMESPACE, DIMENSION_KEY, DIMENSION_VALUE, start, end, PERIOD);
		
		// Verify the number of mock function calls
		ArgumentCaptor<Metric> metricArgument = ArgumentCaptor.forClass(Metric.class);
		verify(experimentRepositoryMock, times(1)).findOne(EXPERIMENT_ID);
		verifyNoMoreInteractions(experimentRepositoryMock);
		verify(metricRepositoryMock, times(1)).saveAndFlush(metricArgument.capture());
		verifyNoMoreInteractions(metricRepositoryMock);
		
		// Verify the mock function argument
		assertSameMetric(persisted, metricArgument.getValue());
		
		// Verify the operation return value
		assertEquals(persisted, returned);
		
		// The Associated Experiment now should have the added Metric in its Metrics list
		assertTrue(associatedExp.getMetrics().contains(metricArgument.getValue()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void createCustomExperimentNull() throws IllegalArgumentException {
		// Set up the mock object behaviour
		when(experimentRepositoryMock.findOne(EXPERIMENT_ID)).thenReturn(null);
		
		// Execute the operation
		service.createNewCustomMetric(EXPERIMENT_ID, NAME, NAMESPACE, DIMENSION_KEY, DIMENSION_VALUE, start, end, PERIOD);
		
		// Verify the number of mock function calls
		verify(experimentRepositoryMock, times(1)).findOne(EXPERIMENT_ID);
		verifyNoMoreInteractions(experimentRepositoryMock);
		verifyNoMoreInteractions(metricRepositoryMock);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void createCustomInvalidNamespace() throws IllegalArgumentException {
		// Set up the mock object behaviour
		Experiment associatedExp = new Experiment();
		associatedExp.setId(EXPERIMENT_ID);
	
		when(experimentRepositoryMock.findOne(EXPERIMENT_ID)).thenReturn(associatedExp);
		String invalidNamespace = "Amazon";
		
		// Execute the operation
		service.createNewCustomMetric(EXPERIMENT_ID, NAME, invalidNamespace, DIMENSION_KEY, DIMENSION_VALUE, start, end, PERIOD);
		
		// Verify the number of mock function calls
		verify(experimentRepositoryMock, times(1)).findOne(EXPERIMENT_ID);
		verifyNoMoreInteractions(experimentRepositoryMock);
		verifyNoMoreInteractions(metricRepositoryMock);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void createCustomInvalidStartEndTime() throws IllegalArgumentException {
		// Set up the mock object behaviour
		Experiment associatedExp = new Experiment();
		associatedExp.setId(EXPERIMENT_ID);
	
		when(experimentRepositoryMock.findOne(EXPERIMENT_ID)).thenReturn(associatedExp);
		Date invalidStart = new Date(END_TIMESTAMP);
		Date invalidEnd = new Date(START_TIMESTAMP);
		
		// Execute the operation
		service.createNewCustomMetric(EXPERIMENT_ID, NAME, NAMESPACE, DIMENSION_KEY, DIMENSION_VALUE, invalidStart, invalidEnd, PERIOD);
		
		// Verify the number of mock function calls
		verify(experimentRepositoryMock, times(1)).findOne(EXPERIMENT_ID);
		verifyNoMoreInteractions(experimentRepositoryMock);
		verifyNoMoreInteractions(metricRepositoryMock);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void createCustomInvalidPeriod() throws IllegalArgumentException {
		// Set up the mock object behaviour
		Experiment associatedExp = new Experiment();
		associatedExp.setId(EXPERIMENT_ID);
	
		when(experimentRepositoryMock.findOne(EXPERIMENT_ID)).thenReturn(associatedExp);
		Long invalidPeriod = -PERIOD;
		
		// Execute the operation
		service.createNewCustomMetric(EXPERIMENT_ID, NAME, NAMESPACE, DIMENSION_KEY, DIMENSION_VALUE, start, end, invalidPeriod);
		
		// Verify the number of mock function calls
		verify(experimentRepositoryMock, times(1)).findOne(EXPERIMENT_ID);
		verifyNoMoreInteractions(experimentRepositoryMock);
		verifyNoMoreInteractions(metricRepositoryMock);
	}
	
	@Test
	public void collectMetricDataFromCloudWatch() 
			throws ProviderException, IllegalArgumentException, MetricNotFoundException {
		// Set up mock object behaviour
		Metric updated = new Metric(TYPE_CUSTOM, NAME, NAMESPACE, DIMENSIONS, start, end, PERIOD);
		updated.setId(METRIC_ID);
		
		Experiment associatedExp = new Experiment();
		associatedExp.setId(EXPERIMENT_ID);
		associatedExp.addMetrics(updated);
		
		when(metricRepositoryMock.findOne(METRIC_ID)).thenReturn(updated);
		
		// Execute the operation
		Metric returned = service.collectMetricDataFromCloudWatch(METRIC_ID);
		
		// Verify number of mock function calls
		verify(metricRepositoryMock, times(1)).findOne(METRIC_ID);
		verifyNoMoreInteractions(metricRepositoryMock);
		verify(awsCloudWatchServiceMock, times(1)).collectMetric(updated);
		verifyNoMoreInteractions(awsCloudWatchServiceMock);
		
		// Verify return value of operation
		assertEquals(updated, returned);
	}
	
	@Test(expected = MetricNotFoundException.class)
	public void collectMetricDataMetricNotFound() 
			throws ProviderException, IllegalArgumentException, MetricNotFoundException {
		// Set up mock object behaviour
		when(metricRepositoryMock.findOne(METRIC_ID)).thenReturn(null);
		
		// Execute the operation
		service.collectMetricDataFromCloudWatch(METRIC_ID);
		
		// Verify the number of mock function calls
		verify(metricRepositoryMock, times(1)).findOne(METRIC_ID);
		verifyNoMoreInteractions(metricRepositoryMock);
		verifyNoMoreInteractions(awsCloudWatchServiceMock);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void collectMetricDataCloudWatchThrowIllegalArgumentException() 
			throws ProviderException, IllegalArgumentException, MetricNotFoundException {
		// Set up mock object behaviour
		Metric metric = new Metric();
		metric.setId(METRIC_ID);
		
		when(metricRepositoryMock.findOne(METRIC_ID)).thenReturn(metric);
		doThrow(IllegalArgumentException.class).when(awsCloudWatchServiceMock).collectMetric(any(Metric.class));
		
		// Execute the operation
		service.collectMetricDataFromCloudWatch(METRIC_ID);
		
		// Verify the number of mock function calls
		//verify(metricRepositoryMock, times(1)).findOne(METRIC_ID);
		verifyZeroInteractions(metricRepositoryMock);
		//verify(awsCloudWatchServiceMock, times(1)).collectMetric(metric);
		verifyZeroInteractions(awsCloudWatchServiceMock);
	}
	
	@Test
	public void delete() 
			throws MetricNotFoundException {
		// Set up mock object behaviour
		Metric deleted = new Metric(TYPE_CUSTOM, NAME, NAMESPACE, DIMENSIONS, start, end, PERIOD);
		deleted.setId(METRIC_ID);
		
		Experiment associatedExp = new Experiment();
		associatedExp.setId(EXPERIMENT_ID);
		associatedExp.addMetrics(deleted);
		
		when(metricRepositoryMock.findOne(METRIC_ID)).thenReturn(deleted);
		
		// Execute the operation
		Metric returned = service.deleteMetric(METRIC_ID);
		
		// Verify the number of mock function calls
		verify(metricRepositoryMock, times(1)).findOne(METRIC_ID);
		verify(metricRepositoryMock, times(1)).delete(deleted);
		verifyNoMoreInteractions(metricRepositoryMock);
		
		// Verify the operation return value
		assertEquals(deleted, returned);
		
		// The Metric has been deleted, so its reference in the Metrics List of the associated Experiment
		// should disappear
		assertTrue(associatedExp.getMetrics().isEmpty());
		assertFalse(associatedExp.getMetrics().contains(deleted));
	}
	
	@Test(expected = MetricNotFoundException.class)
	public void deleteWhenMetricIsNotFound() throws MetricNotFoundException {
		// Set up mock object behaviour
		when(metricRepositoryMock.findOne(METRIC_ID)).thenReturn(null);
		
		// Execute the operation
		service.deleteMetric(METRIC_ID);
		
		// Verify the number of mock function calls
		verify(metricRepositoryMock, times(1)).findOne(METRIC_ID);
		verifyNoMoreInteractions(metricRepositoryMock);
	}
		
	@Test
	public void findById() {
		// set up mock object behaviour
		Metric metric = new Metric(TYPE_CUSTOM, NAME, NAMESPACE, DIMENSIONS, start, end, PERIOD);
		metric.setId(METRIC_ID);
		
		Experiment associatedExp = new Experiment();
		associatedExp.setId(EXPERIMENT_ID);
		associatedExp.addMetrics(metric);
		
		when(metricRepositoryMock.findOne(METRIC_ID)).thenReturn(metric);
		
		// Execute the operation
		Metric returned = service.getMetricById(METRIC_ID);
		
		// Verify the number of mock function calls
		verify(metricRepositoryMock, times(1)).findOne(METRIC_ID);
		verifyNoMoreInteractions(metricRepositoryMock);
		
		// Verify the return value of operation
		assertEquals(metric, returned);
	}
	
	@Test
	public void findAll() {
		// set up mock object behaviour
		List<Metric> metrics = new ArrayList<Metric>();
		
		when(metricRepositoryMock.findAll()).thenReturn(metrics);
		
		// Execute the operation
		List<Metric> returned = service.getAllMetrics();
		
		// Verify the number of mock function calls
		verify(metricRepositoryMock, times(1)).findAll();
		verifyNoMoreInteractions(metricRepositoryMock);
		
		// Verify the return value of opeation
		assertEquals(metrics, returned);
	}
	
	private void assertSameMetric(Metric expected, Metric actual) {
		assertEquals(expected.getExperiment(), actual.getExperiment());
		assertEquals(expected.getType(), actual.getType());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getNamespace(), actual.getNamespace());
		assertEquals(expected.getDimensions(), actual.getDimensions());
		assertEquals(expected.getStatistics(), actual.getStatistics());
		assertEquals(expected.getStartTime(), actual.getStartTime());
		assertEquals(expected.getEndTime(), actual.getEndTime());
		assertEquals(expected.getPeriod(), actual.getPeriod());
	}
}
