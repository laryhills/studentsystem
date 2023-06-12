package com.laryhills.studentsystem.controller;

import com.laryhills.studentsystem.model.Student;
import com.laryhills.studentsystem.model.User;
import com.laryhills.studentsystem.security.services.UserDetailsServiceImpl;
import com.laryhills.studentsystem.service.StudentService;
import com.laryhills.studentsystem.utils.response.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v2/users")
public class UserController {

  private final UserDetailsServiceImpl userDetailsService; // for dependency injection

  public UserController(UserDetailsServiceImpl userDetailsService) {
    // userDetailsService is injected here
    this.userDetailsService = userDetailsService;
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Map<String, Object>> getAllUsers() {
    List<User> users = userDetailsService.getAllUsers();

    if (users.isEmpty()) {
      Map<String, Object> response = ResponseUtils.createResponse("success", "No users found", new ArrayList<>());
      return ResponseEntity.ok(response);
    }

    Map<String, Object> response = ResponseUtils.createResponse("success", "Users retrieved successfully", users);
    return ResponseEntity.ok(response);
  }

}
