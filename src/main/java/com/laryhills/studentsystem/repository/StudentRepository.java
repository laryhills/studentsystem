package com.laryhills.studentsystem.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.laryhills.studentsystem.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

  @Query("SELECT s FROM Student s WHERE s.email = ?1")
  Optional<Student> findStudentByEmail(String email);

  @Query("SELECT s FROM Student s WHERE s.name LIKE %?1% OR s.email LIKE %?1% OR s.phone LIKE %?1% OR s.address LIKE %?1%")
  Optional<List<Student>> findStudentBySearchTerm(String searchTerm);

}
