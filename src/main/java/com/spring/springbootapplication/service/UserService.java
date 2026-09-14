package com.spring.springbootapplication.service;

import com.spring.springbootapplication.dto.RegisterRequest;
import com.spring.springbootapplication.dto.ProfileEditRequest;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final ImageStorageService imageStorageService;

  public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder,ImageStorageService imageStorageService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.imageStorageService = imageStorageService;
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

  @Transactional
  public void updateProfile(
         String email,
         ProfileEditRequest request,
         MultipartFile avatarImage) {

    User user = findByEmail(email);
    user.setIntroduction(request.introduction());

    // 新しい画像が選択されている場合だけ画像を更新
    if (avatarImage != null && !avatarImage.isEmpty()) {
      String imageUrl = imageStorageService.save(avatarImage);
      user.setAvatarImageUrl(imageUrl);
    }

    userRepository.save(user);
  }

}
