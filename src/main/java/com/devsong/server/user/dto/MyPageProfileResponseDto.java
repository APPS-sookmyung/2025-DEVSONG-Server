package com.devsong.server.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPageProfileResponseDto {

    private String profileImageUrl;
    private String username;
    private Long studentId;
    private String major;
    private String email;
}