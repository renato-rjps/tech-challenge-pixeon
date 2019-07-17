package br.com.rjps.examapi.controller;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.rjps.examapi.exception.CoinException;
import br.com.rjps.examapi.model.Exam;
import br.com.rjps.examapi.model.HealthcareInstitution;
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
		Optional<HealthcareInstitution> institution = institutionRepository.findById(id);
		
		if(!institution.isPresent()) {
			throw new ResourceNotFoundException("There is no Healthcare Institution whith id: " + id);
		}
		
		List<Exam> readExams = institution.get().getExams()
			.stream()
			.filter(Exam::isRead)
			.collect(toList());
		
		Resources<Exam> resources;
		
		boolean cantRead = institution.get().getCoins() == 0 && readExams.isEmpty();
		
		if(cantRead) {
			throw new CoinException();
		}
		
		if(institution.get().getCoins() == 0) {
			resources = new Resources<>(readExams); 
		} else {
			resources = new Resources<>(institution.get().getExams()); 
		}
				
        resources.add(linkTo(methodOn(ExamController.class).getExam(id)).withSelfRel()); 

        return ResponseEntity.ok(resources); 
    }
	
	

}
