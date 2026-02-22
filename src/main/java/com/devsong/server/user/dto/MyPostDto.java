package com.devsong.server.user.dto;
import com.devsong.server.post.entity.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class MyPostDto {
    private final Long id;
    private final String title;      // 제목
    private final String preview;    // 미리보기
    private final Category category; // 카테고리
    private final String username;   // 작성자 이름

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;  // 작성 시간

    private final boolean closed;   // 모집 마감 여부
    private final Long likeCount;        // 좋아요 수
    private final Long comment;     // 댓글 수
}
