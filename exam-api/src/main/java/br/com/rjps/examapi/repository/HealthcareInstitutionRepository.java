package br.com.rjps.examapi.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.rjps.examapi.model.HealthcareInstitution;

public interface HealthcareInstitutionRepository extends CrudRepository<HealthcareInstitution, Long> {

}
