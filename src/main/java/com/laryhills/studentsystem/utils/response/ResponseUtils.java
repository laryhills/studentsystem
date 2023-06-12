package com.laryhills.studentsystem.utils.response;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

import com.laryhills.studentsystem.security.jwt.JwtUtils;

public class ResponseUtils {
  @Autowired
  private static JwtUtils jwtUtils;

  /*
   * public String GetNewJwtToken(String token) {
   * 
   * String username = jwtUtils.getUserNameFromJwtToken(token);
   * String newToken = jwtUtils.generateTokenFromUsername(username);
   * 
   * return newToken;
   * 
   * }
   */

  public static <T> Map<String, Object> createResponse(String status, String message, List<T> data) {
    return createResponse(status, message, data, null);
  }

  public static <T> Map<String, Object> createResponseWithToken(String status, String message, List<T> data,
      String oldToken) {
    String username = jwtUtils.getUserNameFromJwtToken(oldToken);
    Map<String, Object> response = new HashMap<>();
    response.put("status", status);
    response.put("message", message);
    response.put("data", data != null ? data : new ArrayList<>());
    response.put("token", jwtUtils.generateTokenFromUsername(username));
    return response;
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
