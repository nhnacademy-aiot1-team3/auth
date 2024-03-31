package com.nhnacademy.auth.token.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.auth.exception.MismatchedRefreshTokenException;
import com.nhnacademy.auth.member.dto.response.TokenResponseDto;
import com.nhnacademy.auth.token.service.TokenService;
import com.nhnacademy.auth.token.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.nhnacademy.auth.token.util.JwtUtil.*;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtUtil jwtUtil;

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;


    @Override
    public TokenResponseDto tokenIssue(String memberId, Collection<? extends GrantedAuthority> authorities) {
        String accessToken = jwtUtil.createAccessToken(memberId, authorities);
        String refreshToken = createRefreshToken(memberId, authorities);

        return new TokenResponseDto(
                accessToken,
                refreshToken,
                new Date().getTime() + ACCESS_TOKEN_VALID_TIME,
                new Date().getTime() + REFRESH_TOKEN_VALID_TIME
        );
    }

    @Override
    public TokenResponseDto tokenReissue(String refreshToken) {
        Claims claims = jwtUtil.parseClaims(refreshToken);

        String memberId = jwtUtil.getIssueMember(claims);
        String oldRefreshToken = jwtUtil.getRefreshToken(memberId);

        List<SimpleGrantedAuthority> simpleGrantedAuthorities = claims.get("roles",List.class);

        if (!refreshToken.equals(oldRefreshToken)) {
            redisTemplate.delete(REFRESH_TOKEN_PREFIX + memberId);
            throw new MismatchedRefreshTokenException();
        }
        String reIssueAccessToken = jwtUtil.reIssueAccessToken(claims);
        String reissueRefreshToken = createRefreshToken(memberId, simpleGrantedAuthorities);

        return new TokenResponseDto(
                reIssueAccessToken,
                reissueRefreshToken,
                new Date().getTime() + ACCESS_TOKEN_VALID_TIME,
                new Date().getTime() + REFRESH_TOKEN_VALID_TIME
        );
    }

    private String createRefreshToken(String memberId,Collection<? extends GrantedAuthority> authorities) {
        String reissueRefreshToken = jwtUtil.createRefreshToken(memberId, authorities);

        String key = REFRESH_TOKEN_PREFIX + memberId;

        redisTemplate.opsForValue().set(key, reissueRefreshToken);
        redisTemplate.expire(key, REFRESH_TOKEN_VALID_TIME, TimeUnit.MILLISECONDS);

        return reissueRefreshToken;
    }
}
