package com.swlc.ScrumPepperCPU6001.exception;

import com.swlc.ScrumPepperCPU6001.dto.response.ErrorMessageResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant.APPLICATION_ERROR_OCCURRED_MESSAGE;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {Exception.class})
    ResponseEntity<ErrorMessageResponseDTO> handleAnyException(Exception ex, WebRequest webRequest) {
        return new ResponseEntity<>(
                new ErrorMessageResponseDTO(false, 100, APPLICATION_ERROR_OCCURRED_MESSAGE), HttpStatus.OK);
    }

    @ExceptionHandler(value = {AdminException.class})
    ResponseEntity<ErrorMessageResponseDTO> handleAdminException(AdminException ex, WebRequest webRequest) {
        return new ResponseEntity<>(
                new ErrorMessageResponseDTO(false, ex.getStatus(), ex.getMsg()), HttpStatus.OK);
    }
}