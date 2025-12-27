package com.devsong.server.user.dto;

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
    private List<TechStack> techStack;
    private List<TechStack> interests;
    private String content;
    private String profileImage;
}