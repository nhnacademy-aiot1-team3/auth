package com.nhnacademy.auth.oauth.controller;

import com.nhnacademy.auth.member.dto.response.ResponseDto;
import com.nhnacademy.auth.member.dto.response.ResponseHeaderDto;
import com.nhnacademy.auth.member.dto.response.TokenResponseDto;
import com.nhnacademy.auth.oauth.service.OAuthService;
import com.nhnacademy.auth.oauth.service.OAuthServiceResolver;
import com.nhnacademy.auth.token.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthServiceResolver oAuthServiceResolver;
    private final TokenService tokenService;

    @PostMapping("/{domain}/oauth")
    public ResponseEntity<ResponseDto> oAuthLoginForm(@PathVariable("domain") String domain,String code) {
        OAuthService oAuthService = oAuthServiceResolver.getOAuthService(domain);

        String tokenRequestUrl = oAuthService.makeTokenRequestUrl(code);

        String accessToken = oAuthService.getAccessToken(tokenRequestUrl);

        String userInfoRequestUrl = oAuthService.makeUserInfoRequestUrl();
        String userInfo = oAuthService.getUserInfo(userInfoRequestUrl, accessToken);

        List<SimpleGrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),new SimpleGrantedAuthority("ROLE_ADMIN1"));

        TokenResponseDto tokenResponseDto = tokenService.tokenIssue(userInfo,userInfo, grantedAuthorities);

        HttpHeaders headers = new HttpHeaders();

        ResponseHeaderDto responseHeaderDto = new ResponseHeaderDto(0L, "소셜 로그인 성공");

        ResponseDto responseDto = new ResponseDto(responseHeaderDto, tokenResponseDto);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(responseDto);
    }

}
