package com.nicta.metrics.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Data Persistence entity - Metric.
 * 
 * Has ManyToOne relationship with Experiment - a Metric must belong to an Experiment.
 * 
 * A Metric instance represents a specific CloudWatch metric, which is defined by several parameters:
 * 
 * - Type (custom or aggregated)
 * - Name (CPUUtilization)
 * - Namespace (AWS/EC2)
 * - Dimensions (i.e. InstanceId = i-12345678)
 * - Statistics to be collected (Average, Sum etc.)
 * - Start and End Time
 * - Period interval (i.e. Collect metric datapoints every 60 seconds)
 * - JsonData field which contains the Metric data in JSON format
 * 
 * @author anbinhtran
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "id", 
		"experiment", "type", "name",
		"namespace", "dimensions", "statistics",
		"startTime", "endTime", "period", "jsonData" })
@XmlRootElement(name = "metric")
@Entity
public class Metric implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5270438906407156062L;
	
	@Id
	@Column(name = "METRIC_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "EXPERIMENT_ID")
	private Experiment experiment;
	
	private String type;
	private String name;
	private String namespace;
	private String dimensions;
	private String statistics;
	private Date startTime;
	private Date endTime;
	private Long period;
	
	@Lob
	@Column(length = 5242880)
	private String jsonData;
	
	public Metric() {}
	
	public Metric(String type, String name, String namespace,
			String dimensions, Date startTime, Date endTime, 
			Long period) {
		this.type = type;
		this.name = name;
		this.namespace = namespace;
		this.dimensions = dimensions;
		this.startTime = startTime;
		this.endTime = endTime;
		this.period = period;
		
		// Default to collect every possible statistics
		this.statistics = "Average, Minimum, Maximum, Sum, SampleCount";
	}

	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the experiment
	 */
	public Experiment getExperiment() {
		return experiment;
	}

	/**
	 * @param experiment the experiment to set
	 */
	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * @param namespace the namespace to set
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * @return the dimensions
	 */
	public String getDimensions() {
		return dimensions;
	}

	/**
	 * @param dimensions the dimensions to set
	 */
	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}

	/**
	 * @return the statistics
	 */
	public String getStatistics() {
		return statistics;
	}

	/**
	 * @param statistics the statistics to set
	 */
	public void setStatistics(String statistics) {
		this.statistics = statistics;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the period
	 */
	public Long getPeriod() {
		return period;
	}

	/**
	 * @param period the period to set
	 */
	public void setPeriod(Long period) {
		this.period = period;
	}

	/**
	 * @return the jsonData
	 */
	public String getJsonData() {
		return jsonData;
	}

	/**
	 * @param jsonData the jsonData to set
	 */
	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	/**
	 * This setter should be used only by unit test
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
}
