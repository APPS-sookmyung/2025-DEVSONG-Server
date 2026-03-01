package com.devsong.server.user.dto;

import com.devsong.server.user.entity.Interests;
import com.devsong.server.user.entity.TechStack;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateResumeRequestDto {
    private String bojId;
    private String githubId;
    private List<TechStack> techStack; // 기술 스택
    private List<Interests> interests; // 관심분야
    private String content;
    private String profileImage;
}