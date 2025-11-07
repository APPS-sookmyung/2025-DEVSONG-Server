package com.devsong.server.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PostPageResponseDto {
    private List<PostListResponseDto> posts; // 게시글 목록
    private int totalPages; // 전체 페이지 수
    private int currentPage; // 현재 페이지 번호
}