package com.nhnacademy.auth.oauth.service;

public interface OAuthService {

    String makeTokenRequestUrl(String code);

    String makeUserInfoRequestUrl();

    String getAccessToken(String url);

    String getUserInfo(String url, String accessToken);
}
