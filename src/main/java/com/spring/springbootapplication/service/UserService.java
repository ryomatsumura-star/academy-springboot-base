package com.spring.springbootapplication.service;

import com.spring.springbootapplication.dto.RegisterRequest;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.Authentication;

@Service
public class UserService implements UserDetailsService {

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

  public User findByEmail(String email) {
  return userRepository.findByEmail(email)
      .orElseThrow(() -> new UsernameNotFoundException(
          "ユーザーが見つかりません: " + email
      ));
}

@Override
public UserDetails loadUserByUsername(String email)
    throws UsernameNotFoundException {

  return findByEmail(email);
}
}
