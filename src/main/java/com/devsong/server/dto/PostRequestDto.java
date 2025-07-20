package com.devsong.server.dto;

import com.devsong.server.domain.Category;
import com.devsong.server.domain.Post;
import com.devsong.server.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

    @Getter
    @NoArgsConstructor
    public class PostRequestDto {
        private User user;
        private String title;
        private String content;
        private Category category;
        private LocalDateTime createdAt;
        private boolean isClosed;


        @Builder
        public PostRequestDto(User user, String title, String content,
                              Category category, boolean isClosed) {
            this.user = user;
            this.title = title;
            this.content = content;
            this.category = category;
            this.isClosed = isClosed;
        }

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
}
