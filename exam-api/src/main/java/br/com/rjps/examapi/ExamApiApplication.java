package br.com.rjps.examapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ExamApiApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ExamApiApplication.class, args);
	}

}
