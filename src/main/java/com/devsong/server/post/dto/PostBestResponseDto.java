package com.devsong.server.post.dto;

import com.devsong.server.post.entity.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostBestResponseDto {
    private Long postId;
    private String title;
    private String preview;
    private Category category;
    private boolean closed;
}