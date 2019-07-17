package br.com.rjps.examapi.exception;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * Rest API error representation
 * 
 * @author Renato Santos
 *
 */
@AllArgsConstructor
@Getter
public class RestApiError {

	private HttpStatus status;
	private String message;
	private List<String> errors;

	public RestApiError(HttpStatus status, String message, String error) {
		this(status, message, Arrays.asList(error));
	}
}
