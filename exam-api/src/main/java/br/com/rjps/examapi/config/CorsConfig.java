package br.com.rjps.examapi.config;

import static org.springframework.web.cors.CorsConfiguration.ALL;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class CorsConfig {

	@Value("${cors.header:*}")
	private String header;

	@Value("${cors.origin:*}")
	private String origin;

	@Bean
	public CorsFilter corsFilter() {

		log.info("Adding filter CORS to origin: {}", origin);
		log.info("Adding filter CORS to header: {}", header);
		log.info("Adding filter CORS to methods: {}", ALL);
		
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOrigin(header);
		config.addAllowedHeader(origin);
		config.setAllowedMethods(Arrays.asList(ALL));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return new CorsFilter(source);
	}

}
