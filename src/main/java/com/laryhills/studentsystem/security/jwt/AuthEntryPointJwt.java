package com.laryhills.studentsystem.security.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

// The AuthEntryPointJwt class implements AuthenticationEntryPoint interface.
// We override the commence() method. It will be triggered anytime unauthenticated User requests a secured HTTP resource
// and an AuthenticationException is thrown.
// We just need to send the error response with unauthorized error message and 401 status code.
// To send the error response, we’ll leverage the HttpServletResponse class.
// We’ll create a AuthEntryPointJwt class to do this.

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

  private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {
    logger.error("Unauthorized error: {}", authException.getMessage());

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    final Map<String, Object> body = new HashMap<>();
    body.put("status", "failed");
    body.put("error", authException.getMessage());
    body.put("message", "Unauthorized Access");
    body.put("path", request.getServletPath());

//    body.put("error", "Unauthorized");
//    body.put("message", authException.getMessage());

    final ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(), body);
  }
}
