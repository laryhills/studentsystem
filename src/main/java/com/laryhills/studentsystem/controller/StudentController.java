package com.laryhills.studentsystem.controller;

import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.laryhills.studentsystem.model.Student;
import com.laryhills.studentsystem.service.StudentService;
import com.laryhills.studentsystem.utils.response.ResponseUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/students")
@CrossOrigin
public class StudentController {

  private final StudentService studentService; // for dependency injection

  public StudentController(StudentService studentService) {
    // studentService is injected here
    this.studentService = studentService;
  }

  @GetMapping
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public ResponseEntity<Map<String, Object>> getAllStudents(HttpServletRequest request) {

    // get token from request attributes
    String newToken = (String) request.getAttribute("newToken");

    List<Student> students = studentService.getAllStudents();

    if (students.isEmpty()) {
      Map<String, Object> response = ResponseUtils.createResponse("success", "No student found", new ArrayList<>(),
          newToken);
      return ResponseEntity.ok(response);
    }

    Map<String, Object> response = ResponseUtils.createResponse("success", "Students retrieved successfully", students,
        newToken);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Map<String, Object>> addNewStudent(@Valid @RequestBody Student student,
      HttpServletRequest request) {

    // get token from request attributes
    String newToken = (String) request.getAttribute("newToken");

    Optional<Student> studentOptional = studentService.findStudentByEmail(student.getEmail());

    if (studentOptional.isPresent()) {
      Map<String, Object> response = ResponseUtils.createResponse("failed", "Email already taken", null, newToken);
      return ResponseEntity.badRequest().body(response);
    }

    Student addedStudent = studentService.addNewStudent(student);

    List<Student> data = Collections.singletonList(addedStudent);
    Map<String, Object> response = ResponseUtils.createResponse("success", "Student added successfully", data,
        newToken);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{studentId}")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public ResponseEntity<Map<String, Object>> getStudentById(@PathVariable("studentId") String studentId,
      HttpServletRequest request) {

    // get token from request attributes
    String newToken = (String) request.getAttribute("newToken");

    // System.out.println("studentId: " + studentId);
    try {
      Long id = Long.valueOf(studentId);
      Optional<Student> studentOptional = studentService.findStudentById(id);

      if (studentOptional.isEmpty()) {
        Map<String, Object> response = ResponseUtils.createResponse("failed", "Student not found", null, newToken);
        return ResponseEntity.badRequest().body(response);
      }

      Student student = studentOptional.get();
      List<Student> data = List.of(student);
      Map<String, Object> response = ResponseUtils.createResponse("success", "Student retrieved successfully", data,
          newToken);
      return ResponseEntity.ok(response);
    } catch (NumberFormatException e) {
      // Handle the error caused by invalid studentId format
      Map<String, Object> response = ResponseUtils.createResponse("error", "Invalid studentId format: " + studentId,
          null, newToken);
      return ResponseEntity.badRequest().body(response);
    }
  }

  @GetMapping("/search/{searchTerm}")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public ResponseEntity<Map<String, Object>> searchStudents(@PathVariable("searchTerm") String searchTerm,
      HttpServletRequest request) {

    // get token from request attributes
    String newToken = (String) request.getAttribute("newToken");

    Optional<List<Student>> studentOptional = studentService.findStudentBySearchTerm(searchTerm);

    if (studentOptional.isEmpty() || studentOptional.get().isEmpty()) {
      Map<String, Object> response = ResponseUtils.createResponse("failed", "No student found", new ArrayList<>(),
          newToken);
      return ResponseEntity.ok(response);
    }

    List<Student> students = studentOptional.get();
    Map<String, Object> response = ResponseUtils.createResponse("success", "Students retrieved successfully", students,
        newToken);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{studentId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Map<String, Object>> deleteStudentById(@PathVariable("studentId") String studentId,
      HttpServletRequest request) {

    // get token from request attributes
    String newToken = (String) request.getAttribute("newToken");

    try {
      Long id = Long.valueOf(studentId);
      Optional<Student> studentOptional = studentService.findStudentById(id);

      if (studentOptional.isEmpty()) {
        Map<String, Object> response = ResponseUtils.createResponse("failed", "Student not found", null, newToken);
        return ResponseEntity.badRequest().body(response);
      }

      studentService.deleteStudentById(id);
      Map<String, Object> response = ResponseUtils.createResponse("success", "Student deleted successfully", null,
          newToken);
      return ResponseEntity.ok(response);
    } catch (NumberFormatException e) {
      // Handle the error caused by invalid studentId format
      Map<String, Object> response = ResponseUtils.createResponse("error", "Invalid studentId format: " + studentId,
          null, newToken);
      return ResponseEntity.badRequest().body(response);
    }
  }

  @PutMapping("/{studentId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Map<String, Object>> updateStudentById(@PathVariable("studentId") String studentId,
      @RequestBody Student student, HttpServletRequest request) {

    // get token from request attributes
    String newToken = (String) request.getAttribute("newToken");

    try {
      Long id = Long.valueOf(studentId);
      Optional<Student> studentOptional = studentService.findStudentById(id);

      if (studentOptional.isEmpty()) {
        Map<String, Object> response = ResponseUtils.createResponse("failed", "Student not found", null, newToken);
        return ResponseEntity.badRequest().body(response);
      }

      Student existingStudent = studentOptional.get();

      if (!student.getAddress().isEmpty()) {
        existingStudent.setAddress(student.getAddress());
      }

      if (!student.getPhone().isEmpty()) {
        existingStudent.setPhone(student.getPhone());
      }

      Student updatedStudent = studentService.addNewStudent(existingStudent);

      List<Student> data = Collections.singletonList(updatedStudent);
      Map<String, Object> response = ResponseUtils.createResponse("success", "Student updated successfully", data,
          newToken);
      return ResponseEntity.ok(response);

    } catch (NumberFormatException e) {
      // Handle the error caused by invalid studentId format
      Map<String, Object> response = ResponseUtils.createResponse("error", "Invalid studentId format: " + studentId,
          null, newToken);
      return ResponseEntity.badRequest().body(response);
    }
  }

}
