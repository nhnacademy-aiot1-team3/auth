package com.nhnacademy.auth.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T,V> {
    private T header;
    private V body;
}
