package com.nhnacademy.auth.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class LogoutRequestDto {
    private String accessToken;
    private String refreshToken;
}
