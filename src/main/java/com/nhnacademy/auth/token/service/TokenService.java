package com.nhnacademy.auth.token.service;

import com.nhnacademy.auth.member.dto.response.TokenResponseDto;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface TokenService {

    TokenResponseDto tokenIssue(String memberId,String memberEmail, Collection<? extends GrantedAuthority> authorities);

    TokenResponseDto tokenReissue(String refreshToken);

}
