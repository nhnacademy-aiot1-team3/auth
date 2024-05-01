package com.nhnacademy.auth.security.details;

import com.nhnacademy.auth.exception.MemberStateNotAllowException;
import com.nhnacademy.auth.member.adaptor.MemberAdaptor;
import com.nhnacademy.auth.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberAdaptor memberAdaptor;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        MemberDto memberDto = memberAdaptor.getMember(username).orElseThrow(() -> new UsernameNotFoundException("유저가 존재하지않습니다"));
        if (memberDto.getState().equalsIgnoreCase("WAIT")) {
            throw new MemberStateNotAllowException();
        }

        CustomUser customUser = new CustomUser(memberDto);


        return customUser;
    }
}
