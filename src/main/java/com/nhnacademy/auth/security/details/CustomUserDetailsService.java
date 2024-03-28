package com.nhnacademy.auth.security.details;

import com.nhnacademy.auth.member.adaptor.MemberAdaptor;
import com.nhnacademy.auth.member.dto.request.LoginRequestDto;
import com.nhnacademy.auth.member.dto.response.LoginInfoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberAdaptor memberAdaptor;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<SimpleGrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        LoginInfoResponseDto loginInfoResponseDto = memberAdaptor.getMember(username).orElse(null);

        return new User(loginInfoResponseDto.getId(),"$2a$10$.xGqjMDwGMhb1KzAjMQ8V.GoqCw5lk6dXkSCPO/rhSVJMXtNQ5LfG",grantedAuthorities);
    }
}
