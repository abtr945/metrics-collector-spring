package com.nicta.metrics.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Data Persistence entity - Experiment.
 * 
 * Has OneToMany relationship with Metric - an Experiment can have many Metrics associated with it.
 * 
 * An Experiment instance represents a specific Amazon Cloud experiment with a Start and End Time.
 * 
 * An Experiment may involve an Auto Scaling Group, and/or a Load Balancer (ELB).
 * 
 * An Experiment may also involve a Rolling Upgrade activity, which can be defined by
 * specifying the Rolling Upgrade concurrentUpgrades parameter (i.e. the number of instances to
 * be upgraded at the same time), as well as the Rolling Upgrade Start/End times.
 * 
 * @author anbinhtran
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "id",
		"asgName", "elbName",
		"totalInstances", "concurrentUpgrades",
		"rollingStartTime", "rollingEndTime", 
		"dataCollectionStartTime", "dataCollectionEndTime" })
@XmlRootElement(name = "experiment")
@Entity
public class Experiment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1175500473521041027L;
	
	@Id
	@Column(name = "EXPERIMENT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String asgName;
	private String elbName;
	private Long totalInstances;
	private Long concurrentUpgrades;
	private Date rollingStartTime;
	private Date rollingEndTime;
	private Date dataCollectionStartTime;
	private Date dataCollectionEndTime;
	
	@XmlTransient
	@JsonIgnore
	@OneToMany(mappedBy = "experiment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Collection<Metric> metrics = new LinkedHashSet<Metric>();
	
	public Experiment() {}
	
	public Experiment(String asgName, String elbName, 
			Long totalInstances, Long concurrentUpgrades, 
			Date rollingStartTime, Date rollingEndTime,
			Date dataCollectionStartTime, Date dataCollectionEndTime) {
		this.asgName = asgName;
		this.elbName = elbName;
		this.totalInstances = totalInstances;
		this.concurrentUpgrades = concurrentUpgrades;
		this.rollingStartTime = rollingStartTime;
		this.rollingEndTime = rollingEndTime;
		this.dataCollectionStartTime = dataCollectionStartTime;
		this.dataCollectionEndTime = dataCollectionEndTime;
	}

	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the asgName
	 */
	public String getAsgName() {
		return asgName;
	}

	/**
	 * @param asgName the asgName to set
	 */
	public void setAsgName(String asgName) {
		this.asgName = asgName;
	}

	/**
	 * @return the elbName
	 */
	public String getElbName() {
		return elbName;
	}

	/**
	 * @param elbName the elbName to set
	 */
	public void setElbName(String elbName) {
		this.elbName = elbName;
	}

	/**
	 * @return the totalInstances
	 */
	public Long getTotalInstances() {
		return totalInstances;
	}

	/**
	 * @param totalInstances the totalInstances to set
	 */
	public void setTotalInstances(Long totalInstances) {
		this.totalInstances = totalInstances;
	}

	/**
	 * @return the concurrentUpgrades
	 */
	public Long getConcurrentUpgrades() {
		return concurrentUpgrades;
	}

	/**
	 * @param concurrentUpgrades the concurrentUpgrades to set
	 */
	public void setConcurrentUpgrades(Long concurrentUpgrades) {
		this.concurrentUpgrades = concurrentUpgrades;
	}

	/**
	 * @return the rollinStartTime
	 */
	public Date getRollingStartTime() {
		return rollingStartTime;
	}

	/**
	 * @param rollingStartTime the rollingStartTime to set
	 */
	public void setRollingStartTime(Date rollingStartTime) {
		this.rollingStartTime = rollingStartTime;
	}

	/**
	 * @return the rollingEndTime
	 */
	public Date getRollingEndTime() {
		return rollingEndTime;
	}

	/**
	 * @param rollingEndTime the rollingEndTime to set
	 */
	public void setRollingEndTime(Date rollingEndTime) {
		this.rollingEndTime = rollingEndTime;
	}
	
	/**
	 * @return the dataCollectionStartTime
	 */
	public Date getDataCollectionStartTime() {
		return dataCollectionStartTime;
	}

	/**
	 * @param dataCollectionStartTime the dataCollectionStartTime to set
	 */
	public void setDataCollectionStartTime(Date dataCollectionStartTime) {
		this.dataCollectionStartTime = dataCollectionStartTime;
	}

	/**
	 * @return the dataCollectionEndTime
	 */
	public Date getDataCollectionEndTime() {
		return dataCollectionEndTime;
	}

	/**
	 * @param dataCollectionEndTime the dataCollectionEndTime to set
	 */
	public void setDataCollectionEndTime(Date dataCollectionEndTime) {
		this.dataCollectionEndTime = dataCollectionEndTime;
	}

	/**
	 * @return the metrics
	 */
	public Collection<Metric> getMetrics() {
		return metrics;
	}

	/**
	 * @param metrics the metrics to set
	 */
	public void setMetrics(Collection<Metric> metrics) {
		this.metrics = metrics;
	}
	
	public void addMetrics(Metric metric) {
		this.metrics.add(metric);
		metric.setExperiment(this);
	}

	/**
	 * This setter should be used only by unit tests
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

}
