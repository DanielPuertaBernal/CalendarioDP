package com.calendariodp.crosscutting.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ValidacionException extends ResponseStatusException {

    public ValidacionException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}