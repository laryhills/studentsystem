package com.laryhills.studentsystem.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laryhills.studentsystem.security.services.UserDetailsServiceImpl;
import com.laryhills.studentsystem.utils.response.TokenResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AuthTokenFilter extends OncePerRequestFilter {
  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

  // The doFilterInternal() method gets the JWT token from the Authorization
  // header,
  // validates it, parses username from it, loads the user details from the
  // database, and finally sets the user details in Spring Security’s
  // SecurityContext. Spring Security uses the user details to perform
  // authorization checks. We can also access the user details stored in the
  // SecurityContext in our controllers to perform our business logic.
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      // skip for login and register requests
      if (request.getRequestURI().equals("/api/v2/auth/login")
          || request.getRequestURI().equals("/api/v2/auth/register")) {
        filterChain.doFilter(request, response);
        return;
      }

      String jwt = parseJwt(request);

      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // add new token to response body
        String newToken = jwtUtils.generateTokenFromUsername(userDetails.getUsername());

        /*
         * // Create a TokenResponse object with the new token
         * TokenResponse responseBody = new TokenResponse(newToken);
         * 
         * // Convert the TokenResponse object to JSON
         * ObjectMapper objectMapper = new ObjectMapper();
         * String jsonResponse = objectMapper.writeValueAsString(responseBody);
         * 
         * // Set the response content type and write the response body
         * response.setContentType(MediaType.APPLICATION_JSON_VALUE);
         * response.getOutputStream().write(jsonResponse.getBytes(StandardCharsets.UTF_8
         * ));
         * response.getOutputStream().flush();
         */

        // Pass the token to the controller via a request attribute
        request.setAttribute("newToken", newToken);

      } else {
        String errorMessage = jwtUtils.getValidateJwtTokenError(jwt);
        handleInvalidJwtToken(request, response, errorMessage);
        return;
      }
    } catch (Exception e) {
      logger.error("Cannot set user authentication: {}", e);
    }

    filterChain.doFilter(request, response);
  }

  private void handleInvalidJwtToken(HttpServletRequest request, HttpServletResponse response, String errorMessage)
      throws IOException {
    try {
      // set appropriate status code
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

      // Create a response map with the error message
      Map<String, Object> responseBody = new HashMap<>();
      responseBody.put("message", errorMessage);
      responseBody.put("status", "error");
      responseBody.put("data", new ArrayList<>());

      // Set the response content type and write the response body
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
      response.getWriter().flush();
    } catch (Exception e) {
      logger.error("Cannot set user authentication: {}", e);
    }
  }

  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7);
    }

    return null;
  }
}
