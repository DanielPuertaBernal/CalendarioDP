package com.calendariodp.crosscutting.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UsuarioExistenteException extends ResponseStatusException {

    public UsuarioExistenteException(String reason) {
        super(HttpStatus.CONFLICT, reason);
    }
}