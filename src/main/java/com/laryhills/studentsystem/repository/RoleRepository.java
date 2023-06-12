package com.laryhills.studentsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laryhills.studentsystem.model.ERole;
import com.laryhills.studentsystem.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
