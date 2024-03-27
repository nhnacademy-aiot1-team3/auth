package com.nhnacademy.auth.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class LoginRequestDto {
    private String id;
    private String password;
}
