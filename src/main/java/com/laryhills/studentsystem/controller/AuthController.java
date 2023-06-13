package com.laryhills.studentsystem.controller;

import java.util.*;
import java.util.stream.Collectors;

import com.laryhills.studentsystem.utils.response.ResponseUtils;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laryhills.studentsystem.model.ERole;
import com.laryhills.studentsystem.model.Role;
import com.laryhills.studentsystem.model.User;
import com.laryhills.studentsystem.repository.RoleRepository;
import com.laryhills.studentsystem.repository.UserRepository;
import com.laryhills.studentsystem.security.jwt.JwtUtils;
import com.laryhills.studentsystem.security.services.UserDetailsImpl;
import com.laryhills.studentsystem.utils.exceptions.CustomException;
import com.laryhills.studentsystem.utils.request.LoginRequest;
import com.laryhills.studentsystem.utils.request.SignupRequest;
import com.laryhills.studentsystem.utils.response.JwtResponse;

// remember to add roles to  database
// use WebSecurityConfig to specify routes
// use AuthTokenFilter to specify login and register routes, and non-auth routes

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v2/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());

    Map<String, Object> response = ResponseUtils.createResponse("success",
        "User logged in successfully!", Collections.singletonList(new JwtResponse(
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            roles)),
        jwt);
    return ResponseEntity.ok(response);

  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      Map<String, Object> response = ResponseUtils.createResponse("failed", "Username already taken", null);
      return ResponseEntity.badRequest().body(response);
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      Map<String, Object> response = ResponseUtils.createResponse("failed", "Email already taken", null);
      return ResponseEntity.badRequest().body(response);
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
        signUpRequest.getEmail(),
        encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    try {
      if (strRoles == null) {
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
            .orElseThrow(() -> new CustomException("Error: Role is not found."));
        roles.add(userRole);
      } else {
        strRoles.forEach(role -> {
          role = role.toLowerCase();
          switch (role) {
            case "admin" -> {
              Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                  .orElseThrow(() -> new CustomException("Error: Role is not found."));
              roles.add(adminRole);
            }
            case "mod" -> {
              Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                  .orElseThrow(() -> new CustomException("Error: Role is not found."));
              roles.add(modRole);
            }
            default -> {
              throw new CustomException("Error: Role is not found.");
            }
          }
        });
      }

      user.setRoles(roles);
      User addedUser = userRepository.save(user);

      Map<String, Object> response = ResponseUtils.createResponse("success",
          "User registered successfully!",
          Collections.singletonList(addedUser));
      return ResponseEntity.ok(response);

    } catch (CustomException e) {
      // Handle the exception and return a response with the exception message
      Map<String, Object> response = ResponseUtils.createResponseWithException("failed", e.getMessage(), e);
      return ResponseEntity.badRequest().body(response);
    }

  }
}
