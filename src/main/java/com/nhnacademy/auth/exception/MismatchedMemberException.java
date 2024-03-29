package com.nhnacademy.auth.exception;

public class MismatchedMemberException extends RuntimeException {
    private static final String MESSAGE = "해당 토큰의 발급자와 같지않습니다";

    public MismatchedMemberException() {
        super(MESSAGE);
    }
}
