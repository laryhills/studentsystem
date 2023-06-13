package com.laryhills.studentsystem.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "username"),
    @UniqueConstraint(columnNames = "email")
})
@Data // lombok annotation to generate getters and setters
@NoArgsConstructor // lombok annotation to generate no-args constructor
// lombok annotation to generate constructor with all args except id
@AllArgsConstructor

public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank // not null and not empty
  @Size(max = 255)
  private String name;

  @NotBlank // not null and not empty
  @Size(max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 120)
  private String password;

  // @ManyToMany annotation is used to create a many-to-many relationship between
  // User and Role entities
  // FetchType.LAZY means that the collection of roles is not loaded unless they
  // are explicitly accessed
  // @JoinTable annotation is used to define the join/linking table
  // joinColumns defines the column name for the current entity (User)
  // inverseJoinColumns defines the column name for the other entity (Role)
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  public User(String name, String username, String email, String password) {
    this.name = name;
    this.username = username;
    this.email = email;
    this.password = password;
  }

}
