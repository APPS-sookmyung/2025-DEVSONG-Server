package com.devsong.server.user.dto;

import com.devsong.server.user.entity.Interests;
import com.devsong.server.user.entity.TechStack;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumeResponseDto {

    private String profileImage; //프로필사진

    private String username; //실명

    private Long studentId; //학번

    private String major; //전공

    private String bojId; //백준 아이디

    private String githubId; //깃허브 아이디

    private List<TechStack> techStack; //기술스택

    private List<Interests> interests; //관심분야

    private String content; //자기소개

}