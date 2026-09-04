package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.dto.RegisterRequest;
import com.spring.springbootapplication.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.Authentication;

@Controller
public class AuthController {

  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/register")
  public String registerForm(Model model) {
    model.addAttribute("registerRequest", new RegisterRequest("", "", ""));
    return "auth/register";
  }

  @PostMapping("/register")
  public String register(
      @Valid @ModelAttribute RegisterRequest registerRequest,
      BindingResult bindingResult,
      Model model
  ) {
    if (bindingResult.hasErrors()) {
      return "auth/register";
    }

    try {
      userService.register(registerRequest);
      return "redirect:/login?registered";
    } catch (IllegalArgumentException e) {
      model.addAttribute("errorMessage", e.getMessage());
      return "auth/register";
    }
  }

  @GetMapping("/login")
  public String loginForm(HttpSession session, Model model) {
    Object loginError = session.getAttribute("loginError");

    if (loginError != null) {
      model.addAttribute("loginError", loginError);
      session.removeAttribute("loginError");
    }
    return "auth/login";
  }

  @GetMapping("/home")
  public String home(Authentication authentication, Model model) {
    User user = userService.findByEmail(authentication.getName());
    model.addAttribute("user", user);
    return "home";
  }
}