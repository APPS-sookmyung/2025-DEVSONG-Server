package com.devsong.server.dto;

import com.devsong.server.domain.Post;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final boolean isClosed;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = post.getUser().getName();
        this.createdAt = post.getCreatedAt();
        this.isClosed = post.isClosed();
    }
}

