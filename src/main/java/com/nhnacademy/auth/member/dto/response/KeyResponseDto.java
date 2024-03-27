package com.nhnacademy.auth.member.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
public class KeyResponseDto {
    private Header header;
    private Body body;

    @Getter
    @ToString
    @NoArgsConstructor
    public static class Header {
        private Integer resultCode;
        private String resultMessage;
        private boolean isSuccessful;
    }

    @Getter
    @ToString
    @NoArgsConstructor
    public static class Body {
        private String secret;
    }

}
