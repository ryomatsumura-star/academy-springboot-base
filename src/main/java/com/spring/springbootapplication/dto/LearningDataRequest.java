package com.spring.springbootapplication.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LearningDataRequest {

  @NotBlank(message = "項目名は必ず入力してください")
  @Size(max = 50, message = "項目名は50文字以内で入力してください")
  private String name;

  @NotNull(message = "学習時間は必ず入力してください")
  @Min(value = 0, message = "学習時間は0以上の数字で入力してください")
  private Integer studyMinutes;

  public LearningDataRequest() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getStudyMinutes() {
    return studyMinutes;
  }

  public void setStudyMinutes(Integer studyMinutes) {
    this.studyMinutes = studyMinutes;
  }
}