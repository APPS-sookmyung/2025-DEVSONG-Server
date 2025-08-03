package com.devsong.server.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostApplyResponseDto {
    private Long postApplyId;
    private Long userId;
}
