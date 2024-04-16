package com.nhnacademy.auth.oauth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.auth.oauth.adaptor.OAuthAdaptor;
import com.nhnacademy.auth.oauth.dto.GithubUserInfoDto;
import com.nhnacademy.auth.oauth.properties.GithubOAuthProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GithubOAuthService implements OAuthService {
    private final GithubOAuthProperties githubOAuthProperties;
    private final OAuthAdaptor oAuthAdaptor;
    private final ObjectMapper objectMapper;


    @Override
    public String makeTokenRequestUrl(String code) {
        log.info("{}", githubOAuthProperties.getTokenUri());
        return UriComponentsBuilder
                .fromUriString(githubOAuthProperties.getTokenUri())
                .queryParam("client_id", githubOAuthProperties.getClientId())
                .queryParam("client_secret", githubOAuthProperties.getSecret())
                .queryParam("code", code)
                .build().toUriString();
    }

    @Override
    public String makeUserInfoRequestUrl() {
        return UriComponentsBuilder
                .fromUriString(githubOAuthProperties.getUserInfoUri())
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
        ResponseEntity<String> userInfo = oAuthAdaptor.getUserInfo(url, accessToken);
        GithubUserInfoDto githubUserInfoDto = null;
        try {
            githubUserInfoDto = objectMapper.readValue(userInfo.getBody(), GithubUserInfoDto.class);
            log.info("{}", githubUserInfoDto);
        } catch (JsonProcessingException e) {

        }
        return githubUserInfoDto.getEmail();
    }
}
