package com.nicta.metrics.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A wrapper class for representing a List of Metrics.
 * Only used for XML marshalling purposes.
 * 
 * @author anbinhtran
 *
 */
@XmlRootElement(name = "metrics")
@XmlAccessorType(XmlAccessType.FIELD)
public class Metrics {

	@XmlElement(name = "metric")
	private List<Metric> metrics = null;
	
	public Metrics() {}
	
	public Metrics(List<Metric> metrics) {
		this.metrics = metrics;
	}
	
	public List<Metric> getMetrics() {
		return metrics;
	}
	
	public void setMetrics(List<Metric> metrics) {
		this.metrics = metrics;
	}
}
