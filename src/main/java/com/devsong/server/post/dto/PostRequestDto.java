package com.devsong.server.post.dto;

import com.devsong.server.post.entity.Category;
import com.devsong.server.post.entity.Post;
import com.devsong.server.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public class PostRequestDto {
        private User user;
        private String title;
        private String content;
        private Category category;
        private boolean isClosed;


        public Post toEntity() {
            return Post.builder()
                    .user(user)
                    .title(title)
                    .content(content)
                    .category(category)
                    .isClosed(isClosed)
                    .build();
        }
    }
