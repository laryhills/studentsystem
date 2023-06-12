package com.laryhills.studentsystem.security.services;

import com.laryhills.studentsystem.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laryhills.studentsystem.model.User;
import com.laryhills.studentsystem.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    return UserDetailsImpl.build(user);
  }

  public List<User> getAllUsers() {
    // remove password from response and users with admin role
    return userRepository.findAll().stream()
        .filter(user -> !user.getRoles().stream().anyMatch(role -> role.getName().name().equals("ROLE_ADMIN")))
        .collect(Collectors.toList());

  }

}