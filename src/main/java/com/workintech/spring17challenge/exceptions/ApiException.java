package com.workintech.spring17challenge.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiException extends RuntimeException{
    private HttpStatus httpStatus;

    public ApiException(String message, HttpStatus status) {
        super(message);
        this.httpStatus = status;
    }
}