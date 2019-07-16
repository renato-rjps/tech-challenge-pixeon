package br.com.rjps.examapi.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;

import br.com.rjps.examapi.model.HealthcareInstitution;

@Component
public class HealthcareInstitutionEventListener extends AbstractRepositoryEventListener<HealthcareInstitution>{
	
	@Value("${cors.origin:20}")
	private Integer defaultCoinsAmount = 0;

	@Override
	protected void onBeforeCreate(HealthcareInstitution institution) {
		institution.setCoins(defaultCoinsAmount);
	}
}
