package com.currencyExchanger.exceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionController {

    private static final String STATUS = "status";
    private static final String MESSAGE = "message";

    public ResponseEntity<Map<String, Object>> getExceptionRequest(CommonException exception) {
        Map<String, Object> responseException = new HashMap<>();
        responseException.put(STATUS, exception.HTTP_STATUS);
        responseException.put(MESSAGE, exception.MESSAGE);
        return new ResponseEntity<>(responseException, HttpStatus.BAD_REQUEST);
    }
}

