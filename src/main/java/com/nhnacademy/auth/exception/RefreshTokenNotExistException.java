package com.nhnacademy.auth.exception;

public class RefreshTokenNotExistException extends RuntimeException {
    private static final String MESSAGE = "존재하지 않은 토큰 입니다";

    public RefreshTokenNotExistException() {
        super(MESSAGE);
    }


}
