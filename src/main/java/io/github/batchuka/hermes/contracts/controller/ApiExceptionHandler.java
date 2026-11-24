package io.github.batchuka.hermes.contracts.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.batchuka.hermes.contracts.service.ContractNotFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidation(MethodArgumentNotValidException e) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid contract");
		Map<String, String> fields = new LinkedHashMap<>();
		e.getFieldErrors().forEach(error -> fields.put(error.getField(), error.getDefaultMessage()));
		problem.setProperty("fields", fields);
		return problem;
	}

	@ExceptionHandler(ContractNotFoundException.class)
	public ProblemDetail handleNotFound(ContractNotFoundException e) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
	}

}