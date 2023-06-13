package com.laryhills.studentsystem.controller;

import java.util.Map;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laryhills.studentsystem.utils.response.ResponseUtils;

import jakarta.servlet.http.HttpServletRequest;

public class FallbackController implements ErrorController {
  private static final String PATH = "/error";

  @RequestMapping(PATH)
  public ResponseEntity<Map<String, Object>> fallback(HttpServletRequest request) {

    // get token from request attributes
    String newToken = (String) request.getAttribute("newToken");

    Map<String, Object> response = ResponseUtils.createResponse("error", "404 Not Found", null, newToken);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

}
