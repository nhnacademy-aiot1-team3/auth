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

//        LoginInfoResponseDto loginInfoResponseDto = memberAdaptor.getMember(username).orElseThrow(() -> new UsernameNotFoundException("유저가 존재하지않습니다"));
        List<SimpleGrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),new SimpleGrantedAuthority("ROLE_ADMIN1"));

//        return new User(loginInfoResponseDto.getId(), loginInfoResponseDto.getPassword(), grantedAuthorities);
        return new User("admin","$2a$10$rpdKrkJ3E4bJoUNfmI8Z7OmD4bTbs.iPyGGKkRmy5j2Qp/rDJT6gO", grantedAuthorities);
    }
}
