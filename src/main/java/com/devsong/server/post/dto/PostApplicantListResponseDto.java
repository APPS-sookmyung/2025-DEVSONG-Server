package com.devsong.server.post.dto;

import com.devsong.server.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PostApplicantListResponseDto {
    private Long userId;
    private String username;
    private String major;
    private Long studentId;
}
