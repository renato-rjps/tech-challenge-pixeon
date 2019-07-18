package br.com.rjps.examapi.service;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.rjps.examapi.config.MessageKeys;
import br.com.rjps.examapi.exception.CoinException;
import br.com.rjps.examapi.model.Exam;
import br.com.rjps.examapi.model.HealthcareInstitution;
import br.com.rjps.examapi.model.projection.ExamListProjection;
import br.com.rjps.examapi.repository.ExamRepository;
import br.com.rjps.examapi.repository.HealthcareInstitutionRepository;
import lombok.AllArgsConstructor;

/**
 * Service to handle operation when retrieve exams
 * 
 * @author Renato Santos
 *
 */
@AllArgsConstructor
@Service
@Transactional
public class ExamHandlerService {

	private @NotNull MessageService messageService;
	private @NotNull ExamRepository examRepository;
	private @NotNull HealthcareInstitutionRepository institutionRepository;
	private @NotNull ProjectionFactory projectionFactory;
	
	/**
	 * Get one exam given an examId and institutionId.
	 * 
	 * @param examId
	 * @param institutionId
	 * @return {@link Exam}
	 */
	public Exam getExam(Long examId, Long institutionId) {
		Optional<Exam> exam = examRepository.findByIdAndHealthcareInstitution_id(examId, institutionId);
		canReadExam(exam);
		return exam.get();
	}
	
	/**
	 * Get one exam by id
	 * 
	 * @param id
	 * @return {@link Exam}
	 */
	public Exam getExam(Long id) {
		Optional<Exam> exam = examRepository.findById(id);
		canReadExam(exam);
		return exam.get();
	}
	
	/**
	 * Charge one coin from an institution
	 * 
	 * @param institution
	 */
	public void collectCoins(HealthcareInstitution institution){
		Integer budget = institution.getCoins() - 1;
		institution.setCoins(budget);
		institutionRepository.save(institution);
	}
	
	/**
	 * Get all  exam from an institution given an institution id.
	 * @param id 
	 * @return a collection of {@link Exam}
	 */
	public Collection<ExamListProjection> getExamsByInstitutionId(Long id) {
		Optional<HealthcareInstitution> institution = institutionRepository.findById(id);

		if (!institution.isPresent()) {
			throw new ResourceNotFoundException(messageService.get(MessageKeys.EXCEPTION_INTITUTION_NOT_FOUND));
		}
		
		 List<ExamListProjection> exams = examRepository
			.findAllByHealthcareInstitution_id(id)
			.stream()
			.map(exam -> projectionFactory.createProjection(ExamListProjection.class, exam))
			.collect(Collectors.toList());
				
		List<ExamListProjection> readExams =  exams.stream().filter(ExamListProjection::isRead).collect(toList());

		boolean cantRead = institution.get().getCoins() == 0 && readExams.isEmpty();

		if (cantRead) {
			throw new CoinException(messageService.get(MessageKeys.EXCEPTION_COINS));
		}

		if (institution.get().getCoins() == 0) {
			return readExams;
		} 
		
		return exams;
	}	

	/**
	 * Checks if an exam can be read
	 *  
	 * @param exam
	 */
	private void canReadExam(Optional<Exam> exam) {
		
		if (!exam.isPresent()) {
			throw new ResourceNotFoundException(messageService.get(MessageKeys.EXCEPTION_EXAM_NOT_FOUND));
		}

		Exam foundExam = exam.get();

		boolean canRead = foundExam.getHealthcareInstitution().getCoins() == 0 && !foundExam.isRead();
		
		if (canRead) {
			throw new CoinException(messageService.get(MessageKeys.EXCEPTION_COINS));
		}
		
		if (!foundExam.isRead()) {
			updateExamAsRead(foundExam);
		}	
		
	}

	private void updateExamAsRead(Exam foundExam) {
		foundExam.setRead(true);
		examRepository.save(foundExam);
		collectCoins(foundExam.getHealthcareInstitution());
	}
}
