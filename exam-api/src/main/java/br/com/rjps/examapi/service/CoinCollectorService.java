package br.com.rjps.examapi.service;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.rjps.examapi.exception.CoinException;
import br.com.rjps.examapi.model.Exam;
import br.com.rjps.examapi.model.HealthcareInstitution;
import br.com.rjps.examapi.repository.ExamRepository;
import br.com.rjps.examapi.repository.HealthcareInstitutionRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Transactional
public class CoinCollectorService {

	private @NotNull ExamRepository examRepository;
	private @NotNull HealthcareInstitutionRepository institutionRepository;

	public Exam getExam(Long examId, Long institutionId) {
		Optional<Exam> exam = examRepository.findByIdAndHealthcareInstitution_id(examId, institutionId);
		return validateGetProcess(examId, exam);
	}
	
	public Exam getExam(Long id) {
		Optional<Exam> exam = examRepository.findById(id);
		return validateGetProcess(id, exam);
	}
	
	/**
	 * Desconta uma moeda de uma instituição
	 * 
	 * @param institution
	 */
	public void collectCoins(HealthcareInstitution institution){
		Integer budget = institution.getCoins() - 1;
		institution.setCoins(budget);
		institutionRepository.save(institution);
	}
	

	private Exam validateGetProcess(Long examId, Optional<Exam> exam) {
		
		if (!exam.isPresent()) {
			throw new ResourceNotFoundException("No exam found with id: " + examId);
		}

		Exam foundExam = exam.get();

		boolean canRead = foundExam.getHealthcareInstitution().getCoins() == 0 && !foundExam.isRead();
		
		if (canRead) {
			throw new CoinException();
		}
		
		if (!foundExam.isRead()) {
			updateExamAsRead(foundExam);
		}	
		
		return foundExam;
	}

	private void updateExamAsRead(Exam foundExam) {
		foundExam.setRead(true);
		examRepository.save(foundExam);
		collectCoins(foundExam.getHealthcareInstitution());
	}
}
