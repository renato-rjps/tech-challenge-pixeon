package br.com.rjps.examapi.exception;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class RestApiError {

	private HttpStatus status;
	private String message;
	private List<String> errors;

	public RestApiError(HttpStatus status, String message, String error) {
		this(status, message, Arrays.asList(error));
	}

	public RestApiError(HttpStatus status, String message, List<String> errors) {
		this.status = status;
		this.message = message;
		this.errors = errors;
	}
}
