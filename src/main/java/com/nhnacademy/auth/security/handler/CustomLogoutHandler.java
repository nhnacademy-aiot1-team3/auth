package com.nhnacademy.auth.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.auth.member.dto.request.LogoutRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String BLACKLIST_PREFIX = "blacklist:";

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        LogoutRequestDto logoutRequestDto = null;
        try {
            logoutRequestDto = objectMapper.readValue(request.getInputStream(), LogoutRequestDto.class);
        } catch (IOException e) {

        }
        String accessToken = logoutRequestDto.getAccessToken();
        String refreshToken = logoutRequestDto.getRefreshToken();

        redisTemplate.opsForSet().add(BLACKLIST_PREFIX+ accessToken);
        redisTemplate.opsForSet().add(BLACKLIST_PREFIX+ refreshToken);

        redisTemplate.delete(refreshToken);
    }
}
