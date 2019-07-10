package br.com.rjps.pixeon.repositories;

import org.springframework.data.repository.CrudRepository;

import br.com.rjps.pixeon.entities.HealthcareInstitution;

public interface HealthcareInstitutionRepository extends CrudRepository<HealthcareInstitution, Long> {

}
