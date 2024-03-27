package com.nhnacademy.auth.member.adaptor;

import com.nhnacademy.auth.member.dto.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberAdaptor {
    private final RestTemplate restTemplate;

    public LoginRequestDto getMember(String memberId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity requestEntity = new HttpEntity(httpHeaders);

        ResponseEntity<LoginRequestDto> response = restTemplate.exchange(
                "http://192.168.71.45:8090/member/login/{memberId}",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                },
                memberId
        );

        return response.getBody();

    }

}
