package com.senai.conta_bancaria.domain.exceptions;

public class ValidacaoException extends RuntimeException {
    public ValidacaoException(String message) {
        super(message);
    }
}
