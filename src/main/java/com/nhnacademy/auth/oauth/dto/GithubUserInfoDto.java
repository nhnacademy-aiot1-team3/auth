package com.nhnacademy.auth.oauth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class GithubUserInfoDto {
    private String login;
    private String id;
    private String name;
    private String email;
}
