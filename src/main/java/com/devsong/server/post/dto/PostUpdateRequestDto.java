package com.devsong.server.post.dto;

import lombok.Getter;

@Getter
public class PostUpdateRequestDto {
    private Long postId;
    private String title;
    private String content;
}
