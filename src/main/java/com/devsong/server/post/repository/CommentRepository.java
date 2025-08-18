package com.devsong.server.post.repository;

import com.devsong.server.post.dto.CommentResponseDto;
import com.devsong.server.post.entity.Comment;
import com.devsong.server.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Long countByPostId(Long postId);
    List<Comment> findByPostId(Long postId);
}