package com.laryhills.studentsystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laryhills.studentsystem.model.Student;
import com.laryhills.studentsystem.repository.StudentRepository;

@Service
public class StudentService {

  private final StudentRepository studentRepository;

  @Autowired
  public StudentService(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  public Student addNewStudent(Student student) {
    return studentRepository.save(student);
  }

  public Optional<Student> findStudentByEmail(String email) {
    return studentRepository.findStudentByEmail(email);
  }

  public List<Student> getAllStudents() {
    return studentRepository.findAll();
  }

  public Optional<Student> findStudentById(Long studentId) {
    return studentRepository.findById(studentId);
  }

  public Optional<List<Student>> findStudentBySearchTerm(String searchTerm) {
    return studentRepository.findStudentBySearchTerm(searchTerm);
  }

  public void deleteStudentById(Long id) {
    studentRepository.deleteById(id);
  }

}