package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.dto.ProfileEditRequest;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.service.UserService;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ProfileController {

  private final UserService userService;

  public ProfileController(UserService userService) {
      this.userService = userService;
  }

  @GetMapping("/profile/edit")
  public String editProfile(Authentication authentication,Model model) {
    String email = authentication.getName();
    User user = userService.findByEmail(email);

    model.addAttribute(
          "profileEditRequest",
          new ProfileEditRequest(user.getIntroduction())
    );

    model.addAttribute("user", user);

    return "profile/edit";
  }

  @PostMapping("/profile/edit")
  public String updateProfile(
         Authentication authentication,
         @Valid @ModelAttribute("profileEditRequest")
         ProfileEditRequest request,
         BindingResult bindingResult,
         @RequestParam(
                  value = "avatarImage",
                  required = false)
         MultipartFile avatarImage,
         Model model) {

    if (bindingResult.hasErrors()) {
      String email = authentication.getName();
      User user = userService.findByEmail(email);
      model.addAttribute("user", user);
      return "profile/edit";
    }

    String email = authentication.getName();

    userService.updateProfile(
                email,
                request,
                avatarImage
    );

    return "redirect:/home";
  }
}