package com.nhnacademy.auth.member.adaptor;

import com.nhnacademy.auth.member.dto.response.LoginInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;


@FeignClient(value = "account-service")
public interface MemberAdaptor {
    @GetMapping("/member/{memberId}")
    Optional<LoginInfoResponseDto> getMember(@PathVariable("memberId") String memberId);
}

