package com.nhnacademy.auth.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseHeaderDto {
    private Long resultCode;
    private String resultMessage;
}
