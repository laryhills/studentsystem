package com.laryhills.studentsystem.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Sex {
  MALE,
  FEMALE;

  @JsonCreator
  public static Sex fromString(String value) {
    for (Sex sex : Sex.values()) {
      if (sex.name().equalsIgnoreCase(value)) {
        return sex;
      }
    }
    throw new IllegalArgumentException("Invalid sex value: " + value);
  }
}
