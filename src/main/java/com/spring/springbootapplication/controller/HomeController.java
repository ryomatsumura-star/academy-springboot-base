package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.entity.LearningData;
import com.spring.springbootapplication.service.UserService;
import com.spring.springbootapplication.service.LearningDataService;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserService userService;
    private final LearningDataService learningDataService;

    public HomeController(
            UserService userService,
            LearningDataService learningDataService) {

        this.userService = userService;
        this.learningDataService = learningDataService;
    }

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {

        User user = userService.findByEmail(
                authentication.getName()
        );

        List<LearningData> learningDataList =
                learningDataService.findByUser(user);

        Map<String, Integer> categoryMinutes =
                learningDataService.getCategoryMinutes(user);

        //  直近3か月のグラフ用データを取得
        Map<String, List<Integer>> chartData =
                learningDataService.getChartData(user);

        System.out.println("chartData = " + chartData);
        model.addAttribute("chartData", chartData);

        YearMonth currentMonth = YearMonth.now();

        List<String> chartMonths = List.of(
          "先々月",
          "先月",
          "今月"
        );

        model.addAttribute("user", user);
        model.addAttribute("learningDataList", learningDataList);
        model.addAttribute("categoryMinutes", categoryMinutes);

        model.addAttribute("chartMonths", chartMonths);
        model.addAttribute("chartData", chartData);

        return "home";
    }
}