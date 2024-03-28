package com.nhnacademy.auth.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.auth.security.details.CustomUserDetailsService;
import com.nhnacademy.auth.security.filter.CustomAuthenticationFilter;
import com.nhnacademy.auth.security.handler.CustomAuthenticationFailureHandler;
import com.nhnacademy.auth.security.provider.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security 설정 클래스
 *
 * @author : 양현성
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String LOGIN_URL = "/auth/login";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors().disable()
                .formLogin().disable()
                .logout().disable();
        http.addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * 유저의 비밀번호를 암호화, 검증 해주는 빈
     * @return BCryptPasswordEncoder 반환
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 로그인 정보를 받았을 때 인증서버의 dispatcher 이전에서 실행될 필터 메소드 빈
     * @return
     * @throws Exception
     */

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(objectMapper,redisTemplate);

        customAuthenticationFilter.setFilterProcessesUrl(LOGIN_URL);
        customAuthenticationFilter.setAuthenticationManager(getAuthenticationManager(null));
        customAuthenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
        return customAuthenticationFilter;
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        CustomAuthenticationProvider customAuthenticationProvider = new CustomAuthenticationProvider();

        customAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        customAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return customAuthenticationProvider;
    }
}
