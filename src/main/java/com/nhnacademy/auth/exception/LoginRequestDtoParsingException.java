package com.nhnacademy.auth.exception;

public class LoginRequestDtoParsingException extends RuntimeException{
    private static final String MESSAGE = "로그인 요청 정보를 파싱 할 수 없습니다";

    public LoginRequestDtoParsingException() {
        super(MESSAGE);

    }
}
