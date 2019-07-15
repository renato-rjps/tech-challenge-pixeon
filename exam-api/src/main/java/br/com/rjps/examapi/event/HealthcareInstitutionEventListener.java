package br.com.rjps.examapi.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;

import br.com.rjps.examapi.model.HealthcareInstitution;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HealthcareInstitutionEventListener extends AbstractRepositoryEventListener<HealthcareInstitution>{
	
	@Value("${cors.origin:20}")
	private Integer defaultCoinsAmount = 0;

	@Override
	protected void onBeforeCreate(HealthcareInstitution institution) {
		log.info("[HealthcareInstitution]");
		institution.setCoins(defaultCoinsAmount);
	}
}
