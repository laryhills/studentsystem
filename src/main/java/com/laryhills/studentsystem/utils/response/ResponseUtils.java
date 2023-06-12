package com.laryhills.studentsystem.utils.response;

import java.util.*;

public class ResponseUtils {
  public static <T> Map<String, Object> createResponse(String status, String message, List<T> data) {
    return createResponse(status, message, data, null);
  }

  public static <T> Map<String, Object> createResponse(String status, String message, List<T> data, String token) {
    Map<String, Object> response = new HashMap<>();
    response.put("status", status);
    response.put("message", message);
    response.put("data", data != null ? data : new ArrayList<>());
    if (token != null) {
      response.put("token", token);
    }
    return response;
  }

  public static Map<String, Object> createResponseWithException(String status, String message, Exception exception) {
    Map<String, Object> response = new HashMap<>();
    response.put("status", status);
    response.put("message", message);
    response.put("error", exception.getMessage());
    return response;
  }

}
