package br.com.rjps.examapi.config;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.HEAD;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpMethod.TRACE;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ExposureConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

import br.com.rjps.examapi.model.Exam;
import br.com.rjps.examapi.model.HealthcareInstitution;

@Configuration
public class RestConfig implements RepositoryRestConfigurer {
	
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration restConfig) {
		ExposureConfiguration config = restConfig.getExposureConfiguration();
		config.forDomainType(HealthcareInstitution.class)
			.withItemExposure((metadata, httpMethods) -> httpMethods.disable(HEAD, PUT, PATCH, DELETE, OPTIONS, TRACE));
		
		config.forDomainType(Exam.class)
			.withItemExposure((metadata, httpMethods) -> httpMethods.disable(HEAD, PUT, OPTIONS, TRACE));
		
		restConfig.exposeIdsFor(
        		Exam.class, 
        		HealthcareInstitution.class);
	}

	@Bean
	public SpelAwareProxyProjectionFactory projectionFactory() {
		return new SpelAwareProxyProjectionFactory();
	}
	
}
