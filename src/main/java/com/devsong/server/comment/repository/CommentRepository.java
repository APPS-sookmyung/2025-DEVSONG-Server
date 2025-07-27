package com.devsong.server.comment.repository;

import com.devsong.server.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findByCreatedAt(LocalDateTime createdAt);
}

