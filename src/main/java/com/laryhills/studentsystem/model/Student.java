package com.laryhills.studentsystem.model;

import java.time.LocalDate;
import java.time.Period;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Student {

  @Id
  @SequenceGenerator(name = "student_sequence", sequenceName = "student_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_sequence")
  private Long id;

  @NotBlank
  @Size(max = 255)
  private String name;

  @NotBlank
  @Email
  @Size(max = 255)
  private String email;

  @NotBlank
  @NotNull
  @Pattern(regexp = "\\d{11}")
  private String phone;

  @NotBlank
  @NotNull
  @Size(max = 255)
  private String address;

  @NotNull
  @Past
  private LocalDate dob;

  @Transient // not a column in db
  private Integer age;

  @NotNull
  @Enumerated
  private Sex sex;

  public Integer getAge() {
    // from dob, calculate age
    return Period.between(this.dob, LocalDate.now()).getYears();
  }

}
