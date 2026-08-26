package com.spring.springbootapplication.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public String handleIllegalArgument(IllegalArgumentException e, Model model) {
    model.addAttribute("errorMessage", e.getMessage());
    return "error";
  }

  @ExceptionHandler(Exception.class)
  public String handleGeneral(Exception e, Model model) {
    model.addAttribute("errorMessage", "予期しないエラーが発生しました");
    return "error";
  }
}
