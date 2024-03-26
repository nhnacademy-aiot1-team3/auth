package com.nhnacademy.auth.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.auth.member.dto.LoginRequestDto;
import com.nhnacademy.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.nhnacademy.auth.util.JwtUtil.AUTH_HEADER;
import static com.nhnacademy.auth.util.JwtUtil.TOKEN_TYPE;


/**
 * id, password를 받아서 jwt를 발급해주는 필터
 *
 * @author : 양현성
 */
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginRequestDto loginRequestDto = null;
        try {
            loginRequestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequestDto.getId(), loginRequestDto.getPassword());

        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("{}","login success");
        UserDetails principal = (UserDetails) authResult.getPrincipal();

        String accessToken = JwtUtil.createAccessToken(principal.getUsername(), principal.getAuthorities());

        response.setHeader("Login-Success", "true");
        response.setHeader(AUTH_HEADER, TOKEN_TYPE + accessToken);
    }
}
