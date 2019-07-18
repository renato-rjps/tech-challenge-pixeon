package br.com.rjps.examapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import br.com.rjps.examapi.model.Exam;
import br.com.rjps.examapi.model.projection.ExamListProjection;

@RepositoryRestResource(excerptProjection=ExamListProjection.class)
public interface ExamRepository extends CrudRepository<Exam, Long> {
	
	@RestResource(exported=false)
	@Override
	Iterable<Exam> findAll();

	@RestResource(exported=false)
	Optional<Exam> findByIdAndHealthcareInstitution_id(Long id, Long institutionId);
	
	@RestResource(exported=false)
	List<Exam> findAllByHealthcareInstitution_id(Long id);
}
