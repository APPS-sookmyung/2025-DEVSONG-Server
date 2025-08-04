package com.devsong.server.post.dto;

import com.devsong.server.post.entity.Post;
import com.devsong.server.post.dto.CommentResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor
public class PostResponseDto {
    private final Long id;
    private final String title;
    private final String username;
    private final String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;
    private final boolean isClosed;
    private final int like;
    private final int comment;
    private final List<CommentResponseDto> comments;

}

