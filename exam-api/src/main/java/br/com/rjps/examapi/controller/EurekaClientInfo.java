package br.com.rjps.examapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rjps.examapi.config.MessageKeys;
import br.com.rjps.examapi.service.MessageService;

@RestController
public class EurekaClientInfo {

	@Value("${eureka.instance.instanceId}")
	private String instanceId;
	
	@Autowired
	private MessageService messageService;

	@GetMapping("/info")
	public String info() {
		return String.format(messageService.get(MessageKeys.INSTANCE_MESSAGE), instanceId);
	}
}
