package com.devsong.server.postlike.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PostLikeRequestDto {
    private Long userId;
    private Long postId;
}
