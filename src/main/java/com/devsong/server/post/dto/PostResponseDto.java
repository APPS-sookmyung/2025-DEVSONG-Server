package com.devsong.server.post.dto;

import com.devsong.server.post.entity.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;
    private final boolean isClosed;
    private final int like;
    private final int comment;

    public static PostResponseDto from(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getUser().getUsername())
                .createdAt(post.getCreatedAt())
                .isClosed(post.isClosed())
                .like(post.getLike())
                .comment(post.getComment())
                .build();
    }
}

