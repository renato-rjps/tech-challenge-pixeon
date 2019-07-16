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
public class ExamController {
	
	private @NotNull CoinCollectorService coinCollectorService;
	
	@GetMapping("/exams/{id}")
    public @ResponseBody ResponseEntity<?> getExam(@PathVariable("id") Long id) {
		
		Exam exam = coinCollectorService.getExam(id);
		
		Resource<Exam> resources = new Resource<>(exam); 

        resources.add(linkTo(methodOn(ExamController.class).getExam(id)).withSelfRel()); 

        return ResponseEntity.ok(resources); 
    }

}
