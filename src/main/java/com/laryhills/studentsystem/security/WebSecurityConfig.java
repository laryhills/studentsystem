package com.laryhills.studentsystem.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.laryhills.studentsystem.security.jwt.AuthEntryPointJwt;
import com.laryhills.studentsystem.security.jwt.AuthTokenFilter;

@Configuration
@EnableMethodSecurity
// allows Spring to find and automatically apply the class to the global Web
// Security.

// (securedEnabled = true,
// jsr250Enabled = true,
// prePostEnabled = true) // by default
public class WebSecurityConfig {

  @Autowired
  UserDetailsService userDetailsService; // load user details from database

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler; // handle unauthorized requests

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() { // filter requests
    return new AuthTokenFilter();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() { // authenticate users
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setPasswordEncoder(passwordEncoder());
    authProvider.setUserDetailsService(userDetailsService);

    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() { // encode passwords
    return new BCryptPasswordEncoder();
  }

  // What this does is to disable CSRF protection and configure the exception
  // handling to return a 401 error code to clients that try to access a protected
  // resource without proper authentication.
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // Disable CSRF protection
    http.csrf(AbstractHttpConfigurer::disable)

      // Configure authentication entry point for handling authentication failures
      .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))

      // Set session creation policy to STATELESS
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

      // Define access rules for different URLs and endpoints
      .authorizeHttpRequests(auth ->
          auth.requestMatchers("/", "/api/v2", "/error").permitAll()  // Permit access to /, /api/v2 without authentication
            .requestMatchers("/api/v2/auth/**").permitAll() // Permit access to /api/v2/auth/** without authentication
            .requestMatchers("/api/v2/test/**").permitAll() // Permit access to /api/v2/test/** without authentication
//            .requestMatchers("/api/v2/students/**").permitAll() // Example of commented-out access rule
            .anyRequest().authenticated() // Require authentication for any other request
      );

    // Configure authentication provider
    http.authenticationProvider(authenticationProvider());

    // Add custom filter before the UsernamePasswordAuthenticationFilter
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    // Build and return the SecurityFilterChain
    return http.build();
  }


}
