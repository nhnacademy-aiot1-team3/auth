package com.nhnacademy.auth.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class LoginInfoResponseDto {
    private String id;
    private String password;
    private String role;
}
