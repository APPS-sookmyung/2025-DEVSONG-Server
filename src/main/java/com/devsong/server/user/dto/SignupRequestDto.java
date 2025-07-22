package com.devsong.server.user.dto;

import lombok.Getter;

@Getter
public class SignupRequestDto {

    private String email;

    private String password; //비밀번호

    private String username; //실명

    private Long studentId; //학번

    private String major; //전공

    private String bojId; //백준 아이디

    private String githubId; //깃허브 아이디
}
