package com.spring.springbootapplication.service;

import com.spring.springbootapplication.entity.LearningData;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.repository.LearningDataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class LearningDataService {

  private final LearningDataRepository learningDataRepository;

  public LearningDataService(LearningDataRepository learningDataRepository) {
    this.learningDataRepository = learningDataRepository;
  }

  public List<LearningData> findByUser(User user) {
    return learningDataRepository.findByUserId(user.getId());
  }

  public List<LearningData> findByUserAndMonth(
      User user,
      LocalDate studyMonth) {

    return learningDataRepository.findByUserIdAndStudyMonth(
        user.getId(),
        studyMonth
    );
  }

  public Map<String, Integer> getCategoryMinutes(User user) {

    List<LearningData> learningDataList =
        learningDataRepository.findByUserId(user.getId());

    Map<String, Integer> categoryMinutes = new LinkedHashMap<>();

    for (LearningData learningData : learningDataList) {
      String categoryName = learningData.getCategory().getName();
      Integer studyMinutes = learningData.getStudyMinutes();
      categoryMinutes.merge(categoryName, studyMinutes, Integer::sum);
    }

    return categoryMinutes;
  }
}