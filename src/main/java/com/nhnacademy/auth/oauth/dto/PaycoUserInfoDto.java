package com.nhnacademy.auth.oauth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class PaycoUserInfoDto{
    private PaycoUserInfoHeaderDto header;
    private PaycoUserInfoDataDto data;
}
