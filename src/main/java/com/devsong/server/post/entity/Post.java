package com.devsong.server.post.entity;

import com.devsong.server.user.entity.User;
import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //pk

    @ManyToOne(fetch = FetchType.LAZY)
    private User user; //유저

    private String title; //제목

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; //내용

    @Enumerated(EnumType.STRING)
    private Category category; //카테고리

    @Setter
    private boolean closed; //마감여부

    private LocalDateTime createdAt; //작성시각

    @Setter
    private Long likeCount; //좋아요 수

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}