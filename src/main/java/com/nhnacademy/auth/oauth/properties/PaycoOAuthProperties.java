package com.nhnacademy.auth.oauth.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "oauth.payco")
public class PaycoOAuthProperties {
    private String clientId;
    private String secret;
    private String redirectUri;

    private String tokenUri;
    private String userInfoUri;
}
