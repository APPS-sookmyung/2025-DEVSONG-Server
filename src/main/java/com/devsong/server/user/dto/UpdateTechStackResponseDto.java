package com.devsong.server.user.dto;

import com.devsong.server.user.entity.TechStack;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UpdateTechStackResponseDto {
    private Long userId;
    private List<TechStack> techStack;
}
