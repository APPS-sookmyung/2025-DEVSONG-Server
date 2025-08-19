package com.devsong.server.post.dto;

import com.devsong.server.post.entity.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class PostDetailResponseDto {
    private final Long id;
    private final String title;
    private final String username;
    private final String content;
    private final Category category;
    private final String major;
    private final Long studentId; //학번
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;
    private final boolean closed; //마감여부
    private final Long like; //좋아요 수
    private final Long comment; //댓글 수
    private final List<CommentResponseDto> comments; //댓글 리스트

}

