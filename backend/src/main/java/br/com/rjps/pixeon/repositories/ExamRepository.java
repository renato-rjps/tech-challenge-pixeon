package br.com.rjps.pixeon.repositories;

import org.springframework.data.repository.CrudRepository;

import br.com.rjps.pixeon.entities.Exam;

public interface ExamRepository extends CrudRepository<Exam, Long> {

}
