package com.Salone.service.exception;

import com.Salone.service.payload.response.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GloableExceptionalHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> ExceptionHandler(Exception ex,
                                                              WebRequest req){
        ex.printStackTrace();
        ExceptionResponse response=new ExceptionResponse(
                ex.getMessage(),
                req.getDescription(false),
                LocalDateTime.now()

        );
    return  ResponseEntity.ok(response);
    }

}
