package br.com.rjps.examapi.event;

import javax.validation.constraints.NotNull;

import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;

import br.com.rjps.examapi.exception.CoinException;
import br.com.rjps.examapi.model.Exam;
import br.com.rjps.examapi.model.HealthcareInstitution;
import br.com.rjps.examapi.repository.HealthcareInstitutionRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class ExamEventListener extends AbstractRepositoryEventListener<Exam>{
	
	private @NotNull HealthcareInstitutionRepository institutionRepository;
	
	@Override
	protected void onBeforeCreate(Exam exam) {
		HealthcareInstitution institution = exam.getHealthcareInstitution();
		Integer budget  = institution.getCoins();
		
		if (--budget < 0) {
			throw new CoinException();
		}
	}
	
	@Override
	protected void onAfterCreate(Exam exam) {
		HealthcareInstitution institution = exam.getHealthcareInstitution();
		Integer budget  = institution.getCoins();
		institution.setCoins(--budget);		
		institutionRepository.save(institution);
	}
			
}
