package com.nhnacademy.auth.oauth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.auth.oauth.adaptor.OAuthAdaptor;
import com.nhnacademy.auth.oauth.dto.PaycoUserInfoDto;
import com.nhnacademy.auth.oauth.properties.PaycoOAuthProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaycoOAuthService implements OAuthService {
    private final PaycoOAuthProperties paycoOAuthProperties;
    private final OAuthAdaptor oAuthAdaptor;
    private final ObjectMapper objectMapper;

    @Override
    public String makeTokenRequestUrl(String code) {
        return UriComponentsBuilder
                .fromUriString(paycoOAuthProperties.getTokenUri())
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", paycoOAuthProperties.getClientId())
                .queryParam("client_secret", paycoOAuthProperties.getSecret())
                .queryParam("code", code)
                .build().toUriString();
    }

    @Override
    public String makeUserInfoRequestUrl() {
        return UriComponentsBuilder
                .fromUriString(paycoOAuthProperties.getUserInfoUri())
                .build().toUriString();
    }

    @Override
    public String getAccessToken(String url) {
        ResponseEntity<String> tokenResult = oAuthAdaptor.getToken(url);
        try {
            Map<String, String> map = objectMapper.readValue(tokenResult.getBody(), Map.class);
            return map.get("access_token");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getUserInfo(String url, String accessToken) {
        ResponseEntity<String> userInfo = oAuthAdaptor.getUserInfo(url, paycoOAuthProperties.getClientId(), accessToken);
        PaycoUserInfoDto paycoUserInfoDto = null;

        try {
            paycoUserInfoDto = objectMapper.readValue(userInfo.getBody(), PaycoUserInfoDto.class);
            log.info("{}",paycoUserInfoDto);
        } catch (JsonProcessingException e) {

        }
        return paycoUserInfoDto.getData().getMember().getEmail();
    }
}
