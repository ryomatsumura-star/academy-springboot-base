package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.dto.LearningDataEditRequest;
import com.spring.springbootapplication.dto.LearningDataRequest;
import com.spring.springbootapplication.entity.Category;
import com.spring.springbootapplication.entity.LearningData;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.service.CategoryService;
import com.spring.springbootapplication.service.LearningDataService;
import com.spring.springbootapplication.service.UserService;
import com.spring.springbootapplication.dto.LearningDataEditRequest;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
      @RequestParam(required = false)
      YearMonth month,
      Authentication authentication,
      Model model) {

    User user =
        userService.findByEmail(
            authentication.getName()
        );

    YearMonth selectedMonth =
        month != null
            ? month
            : YearMonth.now();

    LocalDate studyMonth =
        selectedMonth.atDay(1);

    List<LearningData> learningDataList =
        learningDataService.findByUserAndMonth(
            user,
            studyMonth
        );

    List<Category> categories =
        categoryService.findAll();

    List<YearMonth> months = List.of(
        YearMonth.now(),
        YearMonth.now().minusMonths(1),
        YearMonth.now().minusMonths(2)
    );

    model.addAttribute(
        "learningDataList",
        learningDataList
    );

    model.addAttribute(
        "categories",
        categories
    );

    model.addAttribute(
        "months",
        months
    );

    model.addAttribute(
        "selectedMonth",
        selectedMonth
    );

    return "skill/edit";
  }

  @GetMapping("/skill/add")
  public String add(
      @RequestParam YearMonth month,
      @RequestParam Long categoryId,
      Model model) {

    Category category =
        categoryService.findById(categoryId);

    model.addAttribute(
        "selectedMonth",
        month
    );

    model.addAttribute(
        "category",
        category
    );

    model.addAttribute(
        "learningDataRequest",
        new LearningDataRequest()
    );

    return "skill/add";
  }

  @PostMapping("/skill/add")
  public String add(
      @Valid
      @ModelAttribute
      LearningDataRequest learningDataRequest,
      BindingResult bindingResult,
      @RequestParam YearMonth month,
      @RequestParam Long categoryId,
      Authentication authentication,
      Model model) {

    Category category =
        categoryService.findById(categoryId);

    // 入力チェック
    if (bindingResult.hasErrors()) {

      model.addAttribute(
          "selectedMonth",
          month
      );

      model.addAttribute(
          "category",
          category
      );

      return "skill/add";
    }

    User user =
        userService.findByEmail(
            authentication.getName()
        );

    LocalDate studyMonth =
        month.atDay(1);

    try {

      learningDataService.register(
          user,
          category,
          studyMonth,
          learningDataRequest
      );

    } catch (IllegalArgumentException e) {

      bindingResult.rejectValue(
          "name",
          "duplicate",
          e.getMessage()
      );

      model.addAttribute(
          "selectedMonth",
          month
      );

      model.addAttribute(
          "category",
          category
      );

      return "skill/add";
    }

    // 登録成功
    model.addAttribute(
        "selectedMonth",
        month
    );

    model.addAttribute(
        "category",
        category
    );

    model.addAttribute(
        "registered",
        true
    );

    model.addAttribute(
        "registeredName",
        learningDataRequest.getName()
    );

    model.addAttribute(
        "registeredMinutes",
        learningDataRequest.getStudyMinutes()
    );

    return "skill/add";
  }

  @PostMapping("/skill/edit")
  public String updateLearningTime(
      @RequestParam Long id,
      @RequestParam YearMonth month,
      @Valid
      @ModelAttribute
      LearningDataEditRequest learningDataEditRequest,
      BindingResult bindingResult,
      Authentication authentication,
      RedirectAttributes redirectAttributes,
      Model model) {

    User user =
        userService.findByEmail(
            authentication.getName()
        );

    LearningData learningData =
        learningDataService.findById(id);

    if (bindingResult.hasErrors()) {

      LocalDate studyMonth =
          month.atDay(1);

      List<LearningData> learningDataList =
          learningDataService.findByUserAndMonth(
              user,
              studyMonth
          );

      List<Category> categories =
          categoryService.findAll();

      List<YearMonth> months = List.of(
          YearMonth.now(),
          YearMonth.now().minusMonths(1),
          YearMonth.now().minusMonths(2)
      );

      model.addAttribute(
          "learningDataList",
          learningDataList
      );

      model.addAttribute(
          "categories",
          categories
      );

      model.addAttribute(
          "months",
          months
      );

      model.addAttribute(
          "selectedMonth",
          month
      );

      model.addAttribute(
          "errorId",
          id
      );

      model.addAttribute(
          "studyMinutesError",
          bindingResult
              .getFieldError("studyMinutes")
              .getDefaultMessage()
      );

      return "skill/edit";
    }

    learningData.setStudyMinutes(
        learningDataEditRequest.getStudyMinutes()
    );

    learningDataService.save(
        learningData
    );

    redirectAttributes.addFlashAttribute(
        "updatedName",
        learningData.getName()
    );

    return "redirect:/skill/edit?month=" + month;
  }
}
