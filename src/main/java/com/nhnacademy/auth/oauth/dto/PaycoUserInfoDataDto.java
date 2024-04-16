package com.nhnacademy.auth.oauth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class PaycoUserInfoDataDto {
    private PaycoUserInfoDataMemberDto member;
}
