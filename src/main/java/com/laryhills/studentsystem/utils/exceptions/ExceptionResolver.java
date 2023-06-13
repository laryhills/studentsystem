package com.laryhills.studentsystem.utils.exceptions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExceptionResolver {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleException(HttpServletRequest request, Exception ex) {
    Map<String, Object> response = new HashMap<>();
    // response.put("timestampz", LocalDateTime.now());
    response.put("status", "error");
    // response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    response.put("message", ex.getMessage());
    // response.put("path", request.getServletPath());
    response.put("data", new ArrayList<>());
    response.put("token", request.getAttribute("newToken"));

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(MissingPathVariableException.class)
  public ResponseEntity<Map<String, Object>> handleMissingPathVariableException(HttpServletRequest request,
      Exception ex) {
    Map<String, Object> response = new HashMap<>();
    response.put("status", "error");
    response.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    response.put("message", "Missing path variable");
    response.put("data", new ArrayList<>());
    response.put("token", request.getAttribute("newToken"));

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<Map<String, Object>> handleNoHandlerFoundException(HttpServletRequest request, Exception ex) {
    Map<String, Object> response = new HashMap<>();
    response.put("status", "error");
    response.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    response.put("message", "Requested resource wasn't found on the server");
    response.put("data", new ArrayList<>());
    response.put("token", request.getAttribute("newToken"));

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

}
