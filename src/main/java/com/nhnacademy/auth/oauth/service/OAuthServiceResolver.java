package com.nhnacademy.auth.oauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuthServiceResolver {
    private final GithubOAuthService githubOAuthService;
    private final PaycoOAuthService paycoOAuthService;

    public OAuthService getOAuthService(String domain) {
        if(domain.equalsIgnoreCase("github")){
            return githubOAuthService;
        }else if(domain.equalsIgnoreCase("payco")){
            return paycoOAuthService;
        }
        return null;
    }
}
