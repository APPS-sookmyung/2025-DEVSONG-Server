package com.devsong.server.post.dto;

import com.devsong.server.post.entity.Post;
import com.devsong.server.post.dto.CommentResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

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
    private final List<CommentResponseDto> comments;

    public static PostResponseDto from(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getUser().getUsername())
                .createdAt(post.getCreatedAt())
                .isClosed(post.isClosed())
                .like(post.getLike())
                .comments(
                        post.getPostCommentList().stream()
                                .map(comment -> CommentResponseDto.fromEntity(comment))
                                .collect(Collectors.toList())
                )
                .build();
    }
}

