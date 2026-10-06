package com.spring.springbootapplication.repository;

import com.spring.springbootapplication.entity.LearningData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LearningDataRepository
    extends JpaRepository<LearningData, Long> {

  List<LearningData> findByUserId(Long userId);

  List<LearningData> findByUserIdAndStudyMonthOrderByIdAsc(
    Long userId,
    LocalDate studyMonth
);

  boolean existsByUserIdAndStudyMonthAndName(
      Long userId,
      LocalDate studyMonth,
      String name
  );
}