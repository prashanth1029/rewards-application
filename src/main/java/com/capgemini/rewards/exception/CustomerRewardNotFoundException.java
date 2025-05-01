package com.capgemini.rewards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerRewardNotFoundException extends RuntimeException {
    public CustomerRewardNotFoundException(String message) {
        super(message);
    }
}