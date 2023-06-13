package com.laryhills.studentsystem.controller;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ControllerAdvice
public class ValidationExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
    BindingResult bindingResult = ex.getBindingResult();
    List<FieldError> fieldErrors = bindingResult.getFieldErrors();

    Map<String, Object> response = new HashMap<>();
    response.put("status", "error");
    response.put("message", "Validation error");

    List<Map<String, String>> errors = new ArrayList<>();
    for (FieldError fieldError : fieldErrors) {
      Map<String, String> errorMap = new HashMap<>();
      errorMap.put("field", fieldError.getField());
      errorMap.put("message", fieldError.getDefaultMessage());
      errors.add(errorMap);
    }
    response.put("errors", errors);

    return response;
  }

  // Add other exception handlers for specific validation-related exceptions if
  // needed

}
