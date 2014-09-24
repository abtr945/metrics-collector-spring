package com.nicta.metrics.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A wrapper class for representing a List of Experiments.
 * Only used for XML marshalling purposes.
 * 
 * @author anbinhtran
 *
 */
@XmlRootElement(name = "experiments")
@XmlAccessorType(XmlAccessType.FIELD)
public class Experiments {

	@XmlElement(name = "experiment")
	private List<Experiment> experiments = null;
	
	public Experiments() {}
	
	public Experiments(List<Experiment> experiments) {
		this.experiments = experiments;
	}
	
	public List<Experiment> getExperiments() {
		return experiments;
	}
	
	public void setExperiments(List<Experiment> experiments) {
		this.experiments = experiments;
	}
}
