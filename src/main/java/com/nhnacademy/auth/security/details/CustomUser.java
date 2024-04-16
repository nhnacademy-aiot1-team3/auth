package com.nhnacademy.auth.security.details;

import com.nhnacademy.auth.member.dto.MemberDto;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@ToString
@Getter
public class CustomUser implements UserDetails {

    private MemberDto memberDto;

    public CustomUser(MemberDto memberDto) {
        this.memberDto = memberDto;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(memberDto.getRole()));
    }

    @Override
    public String getPassword() {
        return memberDto.getMemberPassword();
    }

    @Override
    public String getUsername() {
        return memberDto.getMemberId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
