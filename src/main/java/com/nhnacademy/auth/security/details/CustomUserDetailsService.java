package com.nhnacademy.auth.security.details;

import com.nhnacademy.auth.member.adaptor.MemberAdaptor;
import com.nhnacademy.auth.member.dto.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberAdaptor memberAdaptor;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<SimpleGrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        LoginRequestDto loginRequestDto = memberAdaptor.getMember(username);




        log.info("{}","run User Details Service");
        log.info("{}",loginRequestDto);

        return new User("admin","$2a$10$.xGqjMDwGMhb1KzAjMQ8V.GoqCw5lk6dXkSCPO/rhSVJMXtNQ5LfG",grantedAuthorities);
    }
}
