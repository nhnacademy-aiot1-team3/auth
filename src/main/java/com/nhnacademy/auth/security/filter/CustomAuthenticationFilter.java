package com.nhnacademy.auth.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.auth.exception.LoginRequestDtoParsingException;
import com.nhnacademy.auth.member.dto.request.LoginRequestDto;
import com.nhnacademy.auth.member.dto.response.ResponseDto;
import com.nhnacademy.auth.member.dto.response.ResponseHeaderDto;
import com.nhnacademy.auth.member.dto.response.TokenResponseDto;
import com.nhnacademy.auth.token.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static com.nhnacademy.auth.token.util.JwtUtil.ACCESS_TOKEN_VALID_TIME;
import static com.nhnacademy.auth.token.util.JwtUtil.REFRESH_TOKEN_VALID_TIME;


/**
 * id, password를 받아서 jwt를 발급해주는 필터
 *
 * @author : 양현성
 */
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String LOGIN_SUCCESS = "Login-Success";

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object>redisTemplate;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginRequestDto loginRequestDto;
        try {
            loginRequestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);
        } catch (IOException e) {
            throw new LoginRequestDtoParsingException();
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequestDto.getId(), loginRequestDto.getPassword());

        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        UserDetails principal = (UserDetails) authResult.getPrincipal();

        String accessToken = jwtUtil.createAccessToken(principal.getUsername(), principal.getAuthorities());
        String refreshToken = jwtUtil.createRefreshToken(principal.getUsername(), principal.getAuthorities());


        redisTemplate.opsForValue().set(refreshToken,principal.getUsername());
        redisTemplate.expire(refreshToken, REFRESH_TOKEN_VALID_TIME, TimeUnit.MILLISECONDS);


        TokenResponseDto tokenResponseDto = new TokenResponseDto(
                accessToken,
                refreshToken,
                ACCESS_TOKEN_VALID_TIME,
                REFRESH_TOKEN_VALID_TIME
        );

        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.setHeader(LOGIN_SUCCESS, "true");
        ResponseHeaderDto responseHeaderDto = new ResponseHeaderDto(0L, "로그인 성공");

        ResponseDto<ResponseHeaderDto, TokenResponseDto> responseDto = new ResponseDto<>(responseHeaderDto, tokenResponseDto);


        response.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDto));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        ResponseHeaderDto responseHeaderDto = null;

        if (failed instanceof BadCredentialsException) {
            responseHeaderDto = new ResponseHeaderDto(4L, failed.getMessage());
        } else if (failed instanceof UsernameNotFoundException) {
            responseHeaderDto = new ResponseHeaderDto(5L, failed.getMessage());
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.setHeader(LOGIN_SUCCESS, "false");

        ResponseDto<ResponseHeaderDto, TokenResponseDto> responseDto = new ResponseDto<>(responseHeaderDto, null);

        response.getWriter().println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(responseDto));
    }
}
