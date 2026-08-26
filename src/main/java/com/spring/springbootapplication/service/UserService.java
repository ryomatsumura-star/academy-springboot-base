package com.spring.springbootapplication.service;

import com.spring.springbootapplication.dto.RegisterRequest;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public User register(RegisterRequest request) {
    if (userRepository.findByEmail(request.email()).isPresent()) {
      throw new IllegalArgumentException("このメールアドレスは既に登録されています");
    }

    User user = new User(
        request.name(),
        request.email(),
        passwordEncoder.encode(request.password())
    );

    return userRepository.save(user);
  }
}
