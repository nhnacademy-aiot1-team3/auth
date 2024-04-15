package com.nhnacademy.auth.token.controller;


import com.nhnacademy.auth.member.dto.response.ResponseDto;
import com.nhnacademy.auth.member.dto.response.ResponseHeaderDto;
import com.nhnacademy.auth.member.dto.response.TokenResponseDto;
import com.nhnacademy.auth.token.service.TokenService;
import com.nhnacademy.auth.token.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.nhnacademy.auth.token.util.JwtUtil.TOKEN_TYPE;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/token")
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/reissue")
    public ResponseEntity<ResponseDto> jwtReissue(@RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String refreshToken) {
        TokenResponseDto tokenResponseDto = tokenService.tokenReissue(refreshToken.substring(TOKEN_TYPE.length(),refreshToken.length()));

        HttpHeaders headers = new HttpHeaders();

        ResponseHeaderDto responseHeaderDto = new ResponseHeaderDto(1L, "토큰 재발급 성공");

        ResponseDto responseDto = new ResponseDto(responseHeaderDto, tokenResponseDto);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(responseDto);
    }

}
