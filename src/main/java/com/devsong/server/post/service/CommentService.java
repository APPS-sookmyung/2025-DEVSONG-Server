package com.devsong.server.post.service;

import com.devsong.server.post.dto.CommentRequestDto;
import com.devsong.server.post.dto.CommentResponseDto;
import com.devsong.server.post.entity.Post;
import com.devsong.server.post.repository.CommentRepository;
import com.devsong.server.post.entity.Comment;
import com.devsong.server.post.repository.PostRepository;
import com.devsong.server.user.entity.User;
import com.devsong.server.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResponseDto addComment(CommentRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(dto.getContent())
                .build();

        Comment saved = commentRepository.save(comment);

        return new CommentResponseDto(saved.getId());
    }
}
