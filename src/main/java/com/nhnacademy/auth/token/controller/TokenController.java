package com.nhnacademy.auth.token.controller;


import com.nhnacademy.auth.exception.MismatchedMemberException;
import com.nhnacademy.auth.member.dto.response.ResponseDto;
import com.nhnacademy.auth.member.dto.response.ResponseHeaderDto;
import com.nhnacademy.auth.token.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/token/reissue")
public class TokenController {
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<ResponseDto> jwtReissue(@RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String refreshToken) {
        String memberId = jwtUtil.getRefreshToken(refreshToken);

        Claims claims = jwtUtil.parseClaims(refreshToken);

        String issueMember = jwtUtil.getIssueMember(claims);
        if (!memberId.equals(issueMember)) {
            throw new MismatchedMemberException();
        }
        String reissueToken = jwtUtil.reIssueAccessToken(claims);


        HttpHeaders headers = new HttpHeaders();

        ResponseHeaderDto responseHeaderDto = new ResponseHeaderDto(1L, "토큰 재발급 성공");

        ResponseDto responseDto = new ResponseDto(responseHeaderDto, reissueToken);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(responseDto);
    }

}
