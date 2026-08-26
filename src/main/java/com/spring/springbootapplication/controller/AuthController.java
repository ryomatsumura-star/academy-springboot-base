package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.dto.RegisterRequest;
import com.spring.springbootapplication.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
  public String register(@Valid @ModelAttribute RegisterRequest registerRequest,
                        BindingResult bindingResult,
                        Model model) {
      if (bindingResult.hasErrors()) {
        return "auth/register";
      }

      try {
          userService.register(registerRequest);
          return "redirect:/home";
      } catch (IllegalArgumentException e) {
          model.addAttribute("errorMessage", e.getMessage());
          return "auth/register";
      }
  }

  @GetMapping("/home")
  public String home() {
    return "home";
  }

}
