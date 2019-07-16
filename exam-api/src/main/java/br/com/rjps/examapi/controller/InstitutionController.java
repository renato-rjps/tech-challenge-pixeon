package br.com.rjps.examapi.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.validation.constraints.NotNull;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.rjps.examapi.model.Exam;
import br.com.rjps.examapi.service.CoinCollectorService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RepositoryRestController
public class InstitutionController {
	
	private @NotNull CoinCollectorService coinCollectorService;
	
	@GetMapping("/healthcareInstitutions/{id}/exams/{examId}")
    public @ResponseBody ResponseEntity<?> getExam(@PathVariable("id") Long id, @PathVariable("examId") Long examId) {
		
		Exam exam = coinCollectorService.getExam(examId, id);
		
        Resource<Exam> resources = new Resource<>(exam); 

        resources.add(linkTo(methodOn(InstitutionController.class).getExam(id,examId)).withSelfRel()); 

        return ResponseEntity.ok(resources); 
    }

}
