package com.nhnacademy.auth.token.controller;


import com.nhnacademy.auth.member.dto.response.ResponseDto;
import com.nhnacademy.auth.member.dto.response.ResponseHeaderDto;
import com.nhnacademy.auth.token.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.nhnacademy.auth.token.util.JwtUtil.AUTH_HEADER;
import static com.nhnacademy.auth.token.util.JwtUtil.TOKEN_TYPE;


@Slf4j
@RestController
@RequestMapping("/auth/token/reissue")
public class TokenController {

    @PostMapping
    public ResponseEntity<ResponseDto> jwtReissue(@RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String refreshToken) {
        Claims claims = JwtUtil.parseClaims(refreshToken);

        String reissueToken = JwtUtil.reIssueAccessToken(claims);

        HttpHeaders headers = new HttpHeaders();

        ResponseHeaderDto responseHeaderDto = new ResponseHeaderDto(1L, "토큰 재발급 성공");

        ResponseDto responseDto = new ResponseDto(responseHeaderDto, reissueToken);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(responseDto);
    }

}
