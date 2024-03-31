package com.nhnacademy.auth.exception;

public class MismatchedRefreshTokenException extends RuntimeException {
    private static final String MESSAGE = "저장된 토큰과 맞지 않습니다";

    public MismatchedRefreshTokenException() {
        super(MESSAGE);
    }
}
