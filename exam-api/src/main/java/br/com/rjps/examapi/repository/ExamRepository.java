package br.com.rjps.examapi.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import br.com.rjps.examapi.model.Exam;

public interface ExamRepository extends CrudRepository<Exam, Long> {
	
	@RestResource(exported=false)
	@Override
	Iterable<Exam> findAll();

	@RestResource(exported=false)
	Optional<Exam> findByIdAndHealthcareInstitution_id(Long id, Long institutionId);
	
	@RestResource(exported=false)
	Optional<Exam> findByHealthcareInstitution_id(Long id);
}
