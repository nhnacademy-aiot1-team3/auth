package com.nhnacademy.auth.oauth.adaptor;

import org.springframework.http.ResponseEntity;

public interface OAuthAdaptor {
    ResponseEntity<String> getToken(String url);

    ResponseEntity<String> getUserInfo(String url, String accessToken);
    ResponseEntity<String> getUserInfo(String url,String clientId, String accessToken);
}
