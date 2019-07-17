package br.com.rjps.examapi.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Collection;

import javax.validation.constraints.NotNull;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.rjps.examapi.model.Exam;
import br.com.rjps.examapi.repository.ExamRepository;
import br.com.rjps.examapi.repository.HealthcareInstitutionRepository;
import br.com.rjps.examapi.service.ExamHandlerService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RepositoryRestController
public class InstitutionController {
	
	private @NotNull HealthcareInstitutionRepository institutionRepository;
	private @NotNull ExamRepository examRepository;
	private @NotNull ExamHandlerService examHandlerService;
	
	@GetMapping("/healthcareInstitutions/{id}/exams/{examId}")
    public @ResponseBody ResponseEntity<?> getExam(@PathVariable("id") Long id, @PathVariable("examId") Long examId) {
		
		Exam exam = examHandlerService.getExam(examId, id);
		
        Resource<Exam> resources = new Resource<>(exam); 

        resources.add(linkTo(methodOn(InstitutionController.class).getExam(id,examId)).withSelfRel()); 

        return ResponseEntity.ok(resources); 
    }
	
	
	@GetMapping("/healthcareInstitutions/{id}/exams")
    public @ResponseBody ResponseEntity<?> getExams(@PathVariable("id") Long id) {
		
		Collection<Exam> exams = examHandlerService.getExamsByInstitutionId(id);
		
		Resources<Exam> resources = new Resources<>(exams); 
		
        resources.add(linkTo(methodOn(InstitutionController.class).getExams(id)).withSelfRel()); 

        return ResponseEntity.ok(resources); 
    }
	
	

}
