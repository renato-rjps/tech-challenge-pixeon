package br.com.rjps.examapi.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Componente especializado em tratar exceções da API.
 * 
 * @author Renato Santos
 * 
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> generic(Exception ex, WebRequest request) {
		RestApiError apiError = new RestApiError(BAD_REQUEST, ex.getMessage(), Collections.emptyList());
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}
	
	@ExceptionHandler({ ResourceNotFoundException.class })
	public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		RestApiError apiError = new RestApiError(NOT_FOUND, ex.getMessage(), Collections.emptyList());
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}
	
	@ExceptionHandler({ CoinException.class })
	public ResponseEntity<Object> handleResourceNotFoundException(CoinException ex, WebRequest request) {
		RestApiError apiError = new RestApiError(BAD_REQUEST, ex.getMessage(), Collections.emptyList());
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}
	
	@ExceptionHandler({ TransactionSystemException.class, DataIntegrityViolationException.class })
	public ResponseEntity<Object> handleConstraintViolation(Exception ex, WebRequest request) {
		Throwable cause = getErrorCause(ex);	    
		List<String> errors = getErros(cause);
		RestApiError apiError = new RestApiError(BAD_REQUEST, "Invalid Request", errors);
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		Throwable throwable = ((HttpMessageNotReadableException) ex).getRootCause();
		List<String> erros = new ArrayList<String>();
		
		while (throwable != null) {
			erros.add(throwable.getMessage());
			throwable = throwable.getCause();
		}

		RestApiError apiError = new RestApiError(BAD_REQUEST, "Invalid Request", erros);
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	private List<String> getErros(Throwable cause) {
		
		List<String> errors = new ArrayList<>();
		
		if (cause instanceof ConstraintViolationException) {
	        Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) cause).getConstraintViolations();
	        
	        constraintViolations.forEach(violation -> {
				StringBuilder message = new StringBuilder()
				.append(violation.getPropertyPath())
				.append(": ")
				.append(violation.getMessage());
		
				errors.add(message.toString());
	        });
	    }
		
		return errors;
	}

	private Throwable getErrorCause(Exception ex) {

		if (ex instanceof DataIntegrityViolationException) {
			return ((DataIntegrityViolationException) ex).getRootCause();
		} else if (ex instanceof TransactionSystemException) {
			return ((TransactionSystemException) ex).getRootCause();
		}
		
		return ex.getCause();
	}
}
