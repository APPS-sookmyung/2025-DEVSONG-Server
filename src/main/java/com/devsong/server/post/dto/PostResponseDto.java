package com.devsong.server.post.dto;

import com.devsong.server.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class PostResponseDto {
    private final Long id;
    private final String title;
    private final String author;
    private final String content;
    private final LocalDateTime createdAt;
    private final boolean isClosed;

    public static PostResponseDto from(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getUser().getUsername())
                .createdAt(post.getCreatedAt())
                .isClosed(post.isClosed())
                .build();
    }
}

