package br.com.rjps.examapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EnableDiscoveryClient
@EntityScan(basePackageClasses = { ExamApiApplication.class, Jsr310Converters.class })
public class ExamApiApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ExamApiApplication.class, args);
	}

}
