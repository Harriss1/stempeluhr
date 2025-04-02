package de.karlk.timetracker.worksession;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class WorkSessionNotFoundAdvice {

  @ExceptionHandler(WorkSessionNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String workSessionNotFoundHandler(WorkSessionNotFoundException ex) {
    return ex.getMessage();
  }
}
