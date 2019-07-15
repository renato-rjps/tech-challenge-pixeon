package br.com.rjps.examapi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
public class EurekaClientInfo {
	
	
	@Value("${eureka.instance.instanceId}")
	private String instanceId;
	

	@GetMapping()
	public String buy() {
		return String.format("Exam API id: %s", instanceId);
	}

}
