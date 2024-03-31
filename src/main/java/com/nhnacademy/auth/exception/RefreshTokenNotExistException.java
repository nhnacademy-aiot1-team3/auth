package com.nhnacademy.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class RefreshTokenNotExistException extends RuntimeException {
    private static final String MESSAGE = "존재하지 않은 토큰 입니다";

    public RefreshTokenNotExistException() {
        super(MESSAGE);
    }


}
