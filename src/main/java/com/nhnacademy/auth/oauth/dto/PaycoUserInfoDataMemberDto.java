package com.nhnacademy.auth.oauth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class PaycoUserInfoDataMemberDto {
    private String idNo;
    private String email;
    private String name;
}
