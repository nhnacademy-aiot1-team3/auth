package com.nhnacademy.auth.exception;

public class MemberStatusException extends RuntimeException {
    private static final String MESSAGE = "대기상태입니다";

    public MemberStatusException() {
        super(MESSAGE);
    }
}
