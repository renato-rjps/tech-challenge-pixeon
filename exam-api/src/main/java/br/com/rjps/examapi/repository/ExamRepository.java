package br.com.rjps.examapi.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.rjps.examapi.model.Exam;

public interface ExamRepository extends CrudRepository<Exam, Long> {

}
