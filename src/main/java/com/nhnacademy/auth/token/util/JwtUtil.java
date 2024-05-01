package com.nhnacademy.auth.token.util;

import com.nhnacademy.auth.exception.InvalidClaimsException;
import com.nhnacademy.auth.exception.InvalidTokenException;
import com.nhnacademy.auth.exception.RefreshTokenNotExistException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "jwt")
public class JwtUtil {

    public static final Long ACCESS_TOKEN_VALID_TIME = Duration.ofHours(1).toMillis();
    public static final Long REFRESH_TOKEN_VALID_TIME = Duration.ofDays(1).toMillis();
    private static final String ACCESS_TOKEN = "access-token";
    private static final String REFRESH_TOKEN = "refresh-token";
    public static final String REFRESH_TOKEN_PREFIX = "refresh_token:";


    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_TYPE = "Bearer ";

    private final RedisTemplate<String, Object> redisTemplate;

    private static String secret;


    private String createToken(String memberId,
                               String memberEmail,
                                      Collection<? extends GrantedAuthority> authorities,
                                      String tokenType,
                                      Long tokenValidTime) {

        Claims claims = Jwts.claims().setSubject(tokenType);

        claims.put("memberId", memberId);
        claims.put("memberEmail", memberEmail);
        claims.put("roles", authorities);

        Date now = new Date();

        Key key = new SecretKeySpec(secret.getBytes(),SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(key)
                .compact();

    }

    public String createAccessToken(String memberId,
                                           String memberEmail,
                                           Collection<? extends GrantedAuthority> authorities) {
        return createToken(memberId,memberEmail, authorities, ACCESS_TOKEN, ACCESS_TOKEN_VALID_TIME);
    }

    public String createRefreshToken(String memberId,
                                     String memberEmail,
                                            Collection<? extends GrantedAuthority> authorities) {
        return createToken(memberId,memberEmail, authorities, REFRESH_TOKEN, REFRESH_TOKEN_VALID_TIME);
    }

    public Claims parseClaims(String token) {
        try {
            Key key = new SecretKeySpec(secret.getBytes(),SignatureAlgorithm.HS256.getJcaName());

            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return claimsJws.getBody();
        } catch (SignatureException | MalformedJwtException e) {
            throw new InvalidTokenException("유효하지 않는 토큰입니다");
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("만료된 토큰입니다");
        }
    }

    public String reIssueAccessToken(Claims claims) {
        Date now = new Date();
        Key key = new SecretKeySpec(secret.getBytes(),SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(key)
                .compact();
    }

    public String getRefreshToken(String memberId) {
        Object refreshToken = redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + memberId);
        if (Objects.isNull(refreshToken)) {
            throw new RefreshTokenNotExistException();
        }
        return refreshToken.toString();
    }

    public String getIssueMember(Claims claims) {
        String issueMember = claims.get("memberId", String.class);
        if (Objects.isNull(issueMember)) {
            throw new InvalidClaimsException();
        }
        return issueMember;
    }

    public String getIssueMemberEmail(Claims claims) {
        String issueMember = claims.get("memberEmail", String.class);
        if (Objects.isNull(issueMember)) {
            throw new InvalidClaimsException();
        }
        return issueMember;
    }

}
