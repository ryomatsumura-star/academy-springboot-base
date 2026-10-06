package com.spring.springbootapplication.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class LearningDataUpdateRequest {

  @NotNull(message = "学習時間は必ず入力してください")
  @Min(
      value = 0,
      message = "学習時間は0以上の数字で入力してください"
  )
  private Integer studyMinutes;

  public LearningDataUpdateRequest() {
  }

  public Integer getStudyMinutes() {
    return studyMinutes;
  }

  public void setStudyMinutes(Integer studyMinutes) {
    this.studyMinutes = studyMinutes;
  }
}