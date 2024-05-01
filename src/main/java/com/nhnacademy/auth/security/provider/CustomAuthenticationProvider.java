package com.nhnacademy.auth.security.provider;

import com.nhnacademy.auth.exception.MemberStateNotAllowException;
import com.nhnacademy.auth.security.details.CustomUser;
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

        CustomUser user = (CustomUser) this.getUserDetailsService().loadUserByUsername(memberId);

        if (!this.getPasswordEncoder().matches(memberPwd, user.getPassword())) {
            throw new BadCredentialsException("아이디/비밀번호가 틀렸습니다");
        }
        if (user.getMemberDto().getState().equalsIgnoreCase("WAIT")) {
            throw new MemberStateNotAllowException();
        }

        return new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );

    }

}
