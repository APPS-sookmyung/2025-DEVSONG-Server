package com.devsong.server.post.dto;

import com.devsong.server.post.entity.Category;
import com.devsong.server.post.entity.Post;
import com.devsong.server.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PostCreateRequestDto {
    private String title;
    private String content;
    private Category category;
    private Long userId;
}
