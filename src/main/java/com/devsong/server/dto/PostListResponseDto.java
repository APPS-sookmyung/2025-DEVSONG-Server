package com.devsong.server.dto;

import com.devsong.server.domain.Post;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class PostListResponseDto {
    private final Long id;
    private final String title;
    private final String author;
    private final LocalDateTime createdAt;
    private final boolean isClosed;

    public PostListResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.author = post.getUser().getName();
        this.createdAt = post.getCreatedAt();
        this.isClosed = post.isClosed();
    }
}


