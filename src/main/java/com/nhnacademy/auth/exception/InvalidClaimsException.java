package com.nhnacademy.auth.exception;

public class InvalidClaimsException extends RuntimeException{
    private static final String MESSAGE = "유효하지 않는 클레임 입니다";

    public InvalidClaimsException() {
        super(MESSAGE);
    }

}
