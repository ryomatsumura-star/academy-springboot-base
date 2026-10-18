package com.spring.springbootapplication.service;

import com.spring.springbootapplication.dto.LearningDataRequest;
import com.spring.springbootapplication.entity.Category;
import com.spring.springbootapplication.entity.LearningData;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.repository.LearningDataRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.time.YearMonth;
import java.util.ArrayList;

@Service
public class LearningDataService {

  private final LearningDataRepository learningDataRepository;

  public LearningDataService(
      LearningDataRepository learningDataRepository) {

    this.learningDataRepository = learningDataRepository;
  }

  public List<LearningData> findByUser(User user) {
    return learningDataRepository.findByUserId(user.getId());
  }

  public List<LearningData> findByUserAndMonth(
      User user,
      LocalDate studyMonth) {

    return learningDataRepository.findByUserIdAndStudyMonthOrderByIdAsc(
        user.getId(),
        studyMonth
    );
  }

  public Map<String, Integer> getCategoryMinutes(User user) {

    List<LearningData> learningDataList =
        learningDataRepository.findByUserId(user.getId());

    Map<String, Integer> categoryMinutes =
        new LinkedHashMap<>();

    for (LearningData learningData : learningDataList) {

      String categoryName =
          learningData.getCategory().getName();

      Integer studyMinutes =
          learningData.getStudyMinutes();

      categoryMinutes.merge(
          categoryName,
          studyMinutes,
          Integer::sum
      );
    }

    return categoryMinutes;
  }

  @Transactional
  public void register(
      User user,
      Category category,
      LocalDate studyMonth,
      LearningDataRequest request) {

    boolean exists =
        learningDataRepository
            .existsByUserIdAndStudyMonthAndName(
                user.getId(),
                studyMonth,
                request.getName()
            );

    if (exists) {
      throw new IllegalArgumentException(
          request.getName()
              + "は既に登録されています"
      );
    }

    LearningData learningData =
        new LearningData();

    learningData.setUser(user);
    learningData.setCategory(category);
    learningData.setName(request.getName());
    learningData.setStudyMonth(studyMonth);
    learningData.setStudyMinutes(
        request.getStudyMinutes()
    );

    learningDataRepository.save(learningData);
  }

  public LearningData save(
      LearningData learningData) {

    return learningDataRepository.save(
        learningData
    );
  }

  public void deleteById(Long id) {
    learningDataRepository.deleteById(id);
  }

  public LearningData findById(Long id) {
    return learningDataRepository.findById(id)
            .orElseThrow();
  }

  public Map<String, List<Integer>> getChartData(User user) {
    YearMonth currentMonth = YearMonth.now();
    List<YearMonth> months = List.of(
            currentMonth.minusMonths(2),
            currentMonth.minusMonths(1),
            currentMonth
    );

    List<String> categories = List.of(
            "バックエンド",
            "フロントエンド",
            "インフラ"
    );

    Map<String, List<Integer>> chartData =
            new LinkedHashMap<>();

    for (String category : categories) {
        chartData.put(category, new ArrayList<>());
    }

    for (YearMonth month : months) {

        List<LearningData> dataList =
                findByUserAndMonth(user, month.atDay(1));

        for (String category : categories) {

            int totalMinutes = dataList.stream()
                    .filter(data ->
                            category.equals(
                                    data.getCategory().getName()
                            ))
                    .mapToInt(LearningData::getStudyMinutes)
                    .sum();

            chartData.get(category).add(totalMinutes);
        }
    }

    return chartData;
  }
}