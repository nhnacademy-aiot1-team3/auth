package com.nhnacademy.auth.token.util;

import com.nhnacademy.auth.config.KeyConfig;
import com.nhnacademy.auth.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "jwt")
public class JwtUtil {

    public static final Long ACCESS_TOKEN_VALID_TIME = Duration.ofHours(1).toMillis();
    public static final Long REFRESH_TOKEN_VALID_TIME = Duration.ofDays(1).toMillis();
    private static final String ACCESS_TOKEN = "access-token";
    private static final String REFRESH_TOKEN = "refresh-token";

    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_TYPE = "Bearer ";

    private final KeyConfig keyConfig;

    private static String secret;


    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @PostConstruct
    private void init() {
        this.secret = keyConfig.keyStorage(secret);
    }

    private static String createToken(String memberId,
                                      Collection<? extends GrantedAuthority> authorities,
                                      String tokenType,
                                      Long tokenValidTime) {

        log.info("{}", secret);

        Claims claims = Jwts.claims().setSubject(tokenType);

        claims.put("memberId", memberId);
        claims.put("roles", authorities);

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

    }

    public static String createAccessToken(String memberId,
                                           Collection<? extends GrantedAuthority> authorities) {
        return createToken(memberId, authorities, ACCESS_TOKEN, ACCESS_TOKEN_VALID_TIME);
    }

    public static String createRefreshToken(String memberId,
                                            Collection<? extends GrantedAuthority> authorities) {
        return createToken(memberId, authorities, REFRESH_TOKEN, REFRESH_TOKEN_VALID_TIME);
    }

    public static Claims parseClaims(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
            return claimsJws.getBody();
        } catch (SignatureException e) {
            throw new InvalidTokenException("유효하지 않는 토큰입니다");
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("만료된 토큰입니다");
        }
    }

    public static String reIssueAccessToken(Claims claims) {
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public static String getRefreshToken(String memberId) {
        return "";
    }
}
