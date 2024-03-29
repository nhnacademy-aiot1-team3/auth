package com.nhnacademy.auth.security.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;

@Slf4j
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String memberId = (String) authentication.getPrincipal();
        String memberPwd = (String) authentication.getCredentials();

        User user = (User) this.getUserDetailsService().loadUserByUsername(memberId);

        if (!this.getPasswordEncoder().matches(memberPwd, user.getPassword())) {
            log.info("{}", "wrong password");
            throw new BadCredentialsException("아이디/비밀번호가 틀렸습니다");
        }

        return new UsernamePasswordAuthenticationToken(
                user,
                user.getPassword(),
                user.getAuthorities()
        );

    }

}
