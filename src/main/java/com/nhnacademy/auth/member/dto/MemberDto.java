package com.nhnacademy.auth.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@AllArgsConstructor
public class MemberDto {
    private Long memberNumber;
    private String memberId;
    private String memberPassword;
    private String memberEmail;
    private String role;
    private String state;
    private LocalDateTime lastLoginDateTime;
}
