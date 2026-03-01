package com.devsong.server.user.dto;

import lombok.Getter;

@Getter
public class MyPageUpdateRequestDto {

    private String username;
    private Long studentId;
    private String major;
    private String email;
}