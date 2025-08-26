package com.devsong.server.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {

    private String message;
    private String token; //jwt
}
