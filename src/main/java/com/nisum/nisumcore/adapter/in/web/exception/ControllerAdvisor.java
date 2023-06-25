package com.nisum.nisumcore.adapter.in.web.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Object> handleBadRequestException(
      BadRequestException ex, WebRequest request) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", "400");
    body.put("error", "BadRequest");
    body.put("exception", ex.getClass());
    body.put("message", ex.getMessage());
    body.put("path", ((ServletWebRequest) request).getRequest().getServletPath());

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<Object> handleConflictException(ConflictException ex, WebRequest request) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", "409");
    body.put("error", "Conflict");
    body.put("exception", ex.getClass());
    body.put("message", ex.getMessage());
    body.put("path", ((ServletWebRequest) request).getRequest().getServletPath());

    return new ResponseEntity<>(body, HttpStatus.CONFLICT);
  }
}
