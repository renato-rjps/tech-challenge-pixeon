package br.com.rjps.examapi.model.projection;

import org.springframework.data.rest.core.config.Projection;

import br.com.rjps.examapi.model.Exam;

@Projection(name = "list-clean", types = { Exam.class })
public interface ExamListProjection {
	
	Long getId();
	String getPatientName();
	boolean isRead();
}
