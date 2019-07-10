package br.com.rjps.pixeon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PixeonApplication {

	public static void main(String[] args) {
		SpringApplication.run(PixeonApplication.class, args);
	}

}
