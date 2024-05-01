package com.nhnacademy.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class MemberStateNotAllowException extends AuthenticationException {
    private static final String MESSAGE = "대기상태입니다";

    public MemberStateNotAllowException() {
        super(MESSAGE);
    }
}
