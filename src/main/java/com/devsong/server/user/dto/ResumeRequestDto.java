package com.devsong.server.user.dto;

import com.devsong.server.user.entity.Interests;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ResumeRequestDto {
    private List<Interests> interests;
    private String content;
    private String profileImage;
}