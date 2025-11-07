package com.devsong.server.user.dto;

import com.devsong.server.user.entity.TechStack;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdateTechStackRequestDto {
    private List<TechStack> techStack;
}