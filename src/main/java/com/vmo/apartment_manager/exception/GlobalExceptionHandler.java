package com.vmo.apartment_manager.exception;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleForeignKeyException(NotFoundException ex)
  {
    log.error("Error", ex);
    return new ErrorResponse(StatusCode.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleBadRequestException(BadRequestException ex){
    log.error("Error", ex);
    return new ErrorResponse(StatusCode.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleOtherException(Exception exception) {
    log.error("Error", exception);
    return new ErrorResponse(StatusCode.INTERNAL_SERVER_ERROR, exception.getMessage());
  }

}
