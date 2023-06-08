package com.laryhills.studentsystem.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.laryhills.studentsystem.model.Student;

public class ResponseUtils {
  public static Map<String, Object> createResponse(String status, String message, List<Student> data) {
    Map<String, Object> response = new HashMap<>();
    response.put("status", status);
    response.put("message", message);
    response.put("data", data != null ? data : new ArrayList<>());
    return response;
  }
}
