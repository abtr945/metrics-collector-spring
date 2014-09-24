package com.nicta.metrics.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nicta.metrics.domain.Experiment;

/**
 * Implements CRUD database operations on Experiment entities
 * 
 * @author anbinhtran
 *
 */
@Repository
public interface ExperimentRepository extends JpaRepository<Experiment, Long> {
	
}
