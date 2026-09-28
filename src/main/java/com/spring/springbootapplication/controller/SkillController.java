package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.entity.Category;
import com.spring.springbootapplication.entity.LearningData;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.service.CategoryService;
import com.spring.springbootapplication.service.LearningDataService;
import com.spring.springbootapplication.service.UserService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SkillController {

  private final UserService userService;
  private final LearningDataService learningDataService;
  private final CategoryService categoryService;

  public SkillController(
      UserService userService,
      LearningDataService learningDataService,
      CategoryService categoryService) {

    this.userService = userService;
    this.learningDataService = learningDataService;
    this.categoryService = categoryService;
  }

  @GetMapping("/skill/edit")
  public String edit(
      @RequestParam(required = false) YearMonth month,
      Authentication authentication,
      Model model) {

    User user = userService.findByEmail(authentication.getName());

    YearMonth selectedMonth =
        month != null ? month : YearMonth.now();

    LocalDate studyMonth =
        selectedMonth.atDay(1);

    List<LearningData> learningDataList =
        learningDataService.findByUserAndMonth(user, studyMonth);

    List<Category> categories =
        categoryService.findAll();

    List<YearMonth> months = List.of(
        YearMonth.now(),
        YearMonth.now().minusMonths(1),
        YearMonth.now().minusMonths(2)
    );

    model.addAttribute("learningDataList", learningDataList);
    model.addAttribute("categories", categories);
    model.addAttribute("months", months);
    model.addAttribute("selectedMonth", selectedMonth);

    return "skill/edit";
  }
}