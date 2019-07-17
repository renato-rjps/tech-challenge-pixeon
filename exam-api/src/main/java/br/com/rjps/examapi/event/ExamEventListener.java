package br.com.rjps.examapi.event;

import javax.validation.constraints.NotNull;

import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;

import br.com.rjps.examapi.config.MessageKeys;
import br.com.rjps.examapi.exception.CoinException;
import br.com.rjps.examapi.model.Exam;
import br.com.rjps.examapi.model.HealthcareInstitution;
import br.com.rjps.examapi.service.ExamHandlerService;
import br.com.rjps.examapi.service.MessageService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class ExamEventListener extends AbstractRepositoryEventListener<Exam>{
	private @NotNull MessageService messageService;
	private @NotNull ExamHandlerService examHandlerService;
	
	@Override
	protected void onBeforeCreate(Exam exam) {
		if(exam.getHealthcareInstitution() == null) {
			return;
		}
		
		HealthcareInstitution institution = exam.getHealthcareInstitution();
		Integer budget  = institution.getCoins() - 1;
		
		if (budget < 0) {
			throw new CoinException(messageService.get(MessageKeys.EXCEPTION_COINS));
		}
	}
	
	@Override
	protected void onAfterCreate(Exam exam) {
		HealthcareInstitution institution = exam.getHealthcareInstitution();
		examHandlerService.collectCoins(institution);
	}
				
}
