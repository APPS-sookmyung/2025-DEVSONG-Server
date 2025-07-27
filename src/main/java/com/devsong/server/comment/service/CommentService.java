package com.devsong.server.comment.service;

import com.devsong.server.comment.dto.CommentRequestDto;
import com.devsong.server.comment.dto.CommentResponseDto;
import com.devsong.server.comment.repository.CommentRepository;
import com.devsong.server.comment.entity.Comment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponseDto addComment(CommentRequestDto dto) {

        Comment comment = Comment.builder()
                        .userId(dto.getUserId())
                        .postId(dto.getPostId())
                        .content(dto.getContent())
                        .build();

        commentRepository.save(comment);

        Long id = commentRepository.findByCreatedAt(comment.getCreatedAt()).getId();

        return new CommentResponseDto(id);
    }
}
