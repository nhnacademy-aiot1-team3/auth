package com.nhnacademy.auth.exception;

public class KeyManagerException extends RuntimeException {
    public static final String MESSAGE = "키 매니저 에러 : ";

    public KeyManagerException(String message) {
        super(MESSAGE + message);
    }
}
