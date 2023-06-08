package com.laryhills.studentsystem.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laryhills.studentsystem.model.Student;
import com.laryhills.studentsystem.service.StudentService;
import com.laryhills.studentsystem.utils.ResponseUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/students")
public class StudentController {

  private final StudentService studentService; // for dependency injection

  public StudentController(StudentService studentService) {
    // studentService is injected here
    this.studentService = studentService;
  }

  @GetMapping
  public ResponseEntity<Map<String, Object>> getAllStudents() {
    List<Student> students = studentService.getAllStudents();

    if (students.isEmpty()) {
      Map<String, Object> response = ResponseUtils.createResponse("success", "No student found", new ArrayList<>());
      return ResponseEntity.ok(response);
    }

    Map<String, Object> response = ResponseUtils.createResponse("success", "Students retrieved successfully", students);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<Map<String, Object>> addNewStudent(@Valid @RequestBody Student student) {
    Optional<Student> studentOptional = studentService.findStudentByEmail(student.getEmail());

    if (studentOptional.isPresent()) {
      Map<String, Object> response = ResponseUtils.createResponse("failed", "Email already taken", null);
      return ResponseEntity.badRequest().body(response);
    }

    Student addedStudent = studentService.addNewStudent(student);

    ;
    List<Student> data = Arrays.asList(addedStudent);
    Map<String, Object> response = ResponseUtils.createResponse("success", "Student added successfully", data);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{studentId}")
  public ResponseEntity<Map<String, Object>> getStudentById(@PathVariable("studentId") String studentId) {
    // System.out.println("studentId: " + studentId);
    try {
      Long id = Long.valueOf(studentId);
      Optional<Student> studentOptional = studentService.findStudentById(id);

      if (!studentOptional.isPresent()) {
        Map<String, Object> response = ResponseUtils.createResponse("failed", "Student not found", null);
        return ResponseEntity.badRequest().body(response);
      }

      Student student = studentOptional.get();
      List<Student> data = Arrays.asList(student);
      Map<String, Object> response = ResponseUtils.createResponse("success", "Student retrieved successfully", data);
      return ResponseEntity.ok(response);
    } catch (NumberFormatException e) {
      // Handle the error caused by invalid studentId format
      Map<String, Object> response = ResponseUtils.createResponse("error", "Invalid studentId format: " + studentId,
          null);
      return ResponseEntity.badRequest().body(response);
    }
  }

  @GetMapping("/search/{searchTerm}")
  public ResponseEntity<Map<String, Object>> searchStudents(@PathVariable("searchTerm") String searchTerm) {
    Optional<List<Student>> studentOptional = studentService.findStudentBySearchTerm(searchTerm);

    if (studentOptional.isEmpty() || studentOptional.get().isEmpty()) {
      Map<String, Object> response = ResponseUtils.createResponse("failed", "No student found", new ArrayList<>());
      return ResponseEntity.ok(response);
    }

    List<Student> students = studentOptional.get();
    Map<String, Object> response = ResponseUtils.createResponse("success", "Students retrieved successfully", students);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{studentId}")
  public ResponseEntity<Map<String, Object>> deleteStudentById(@PathVariable("studentId") String studentId) {
    try {
      Long id = Long.valueOf(studentId);
      Optional<Student> studentOptional = studentService.findStudentById(id);

      if (!studentOptional.isPresent()) {
        Map<String, Object> response = ResponseUtils.createResponse("failed", "Student not found", null);
        return ResponseEntity.badRequest().body(response);
      }

      studentService.deleteStudentById(id);
      Map<String, Object> response = ResponseUtils.createResponse("success", "Student deleted successfully", null);
      return ResponseEntity.ok(response);
    } catch (NumberFormatException e) {
      // Handle the error caused by invalid studentId format
      Map<String, Object> response = ResponseUtils.createResponse("error", "Invalid studentId format: " + studentId,
          null);
      return ResponseEntity.badRequest().body(response);
    }
  }

  @PutMapping("/{studentId}")
  public ResponseEntity<Map<String, Object>> updateStudentById(@PathVariable("studentId") String studentId,
      @RequestBody Student student) {
    try {
      Long id = Long.valueOf(studentId);
      Optional<Student> studentOptional = studentService.findStudentById(id);

      if (!studentOptional.isPresent()) {
        Map<String, Object> response = ResponseUtils.createResponse("failed", "Student not found", null);
        return ResponseEntity.badRequest().body(response);
      }

      Student existingStudent = studentOptional.get();

      if (student.getAddress() != null && !student.getAddress().isEmpty()) {
        existingStudent.setAddress(student.getAddress());
      }

      if (student.getPhone() != null && !student.getPhone().isEmpty()) {
        existingStudent.setPhone(student.getPhone());
      }

      Student updatedStudent = studentService.addNewStudent(existingStudent);

      List<Student> data = Arrays.asList(updatedStudent);
      Map<String, Object> response = ResponseUtils.createResponse("success", "Student updated successfully", data);
      return ResponseEntity.ok(response);

    } catch (NumberFormatException e) {
      // Handle the error caused by invalid studentId format
      Map<String, Object> response = ResponseUtils.createResponse("error", "Invalid studentId format: " + studentId,
          null);
      return ResponseEntity.badRequest().body(response);
    }
  }

}
