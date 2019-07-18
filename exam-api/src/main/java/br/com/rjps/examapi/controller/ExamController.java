package br.com.rjps.examapi.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.validation.constraints.NotNull;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.rjps.examapi.model.Exam;
import br.com.rjps.examapi.model.HealthcareInstitution;
import br.com.rjps.examapi.service.ExamHandlerService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RepositoryRestController
public class ExamController {
	
	private @NotNull ExamHandlerService coinCollectorService;
	private @NotNull RepositoryEntityLinks entityLinks;
	
	
	@GetMapping("/exams/{id}")
    public @ResponseBody ResponseEntity<Resource<Exam>> getExam(@PathVariable("id") Long id) {
				
		Exam exam = coinCollectorService.getExam(id);
		
		Resource<Exam> resource = new Resource<>(exam); 

        resource.add(linkTo(methodOn(ExamController.class).getExam(id)).withSelfRel()); 
        resource.add(entityLinks.linkToSingleResource(Exam.class, id));
        resource.add(entityLinks.linkToSingleResource(HealthcareInstitution.class, exam.getHealthcareInstitution().getId()));
        

        return ResponseEntity.ok(resource); 
    }

}
