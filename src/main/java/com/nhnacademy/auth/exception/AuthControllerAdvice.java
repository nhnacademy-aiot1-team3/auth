package com.nhnacademy.auth.exception;

import com.nhnacademy.auth.member.dto.response.ResponseDto;
import com.nhnacademy.auth.member.dto.response.ResponseHeaderDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class AuthControllerAdvice {
    @ExceptionHandler(value = {
            InvalidTokenException.class,
            InvalidClaimsException.class,
    })
    public ResponseEntity<ResponseDto<ResponseHeaderDto, Object>> invalidTokenExceptionHandler(Exception e) {
        ResponseHeaderDto responseHeaderDto = new ResponseHeaderDto(11L, e.getMessage());

        ResponseDto<ResponseHeaderDto, Object> responseDto = new ResponseDto<>(responseHeaderDto, null);

        return ResponseEntity
                .badRequest()
                .body(responseDto);
    }

    @ExceptionHandler(value = {
            RefreshTokenNotExistException.class,
            MismatchedRefreshTokenException.class,
            MissingRefreshTokenException.class,
    })
    public ResponseEntity<ResponseDto<ResponseHeaderDto,Object>> unauthorized(Exception e) {
        ResponseHeaderDto responseHeaderDto = new ResponseHeaderDto(12L, e.getMessage());
        ResponseDto<ResponseHeaderDto, Object> responseDto = new ResponseDto<>(responseHeaderDto, null);
        return ResponseEntity
                .badRequest()
                .body(responseDto);
    }

}
