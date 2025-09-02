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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResponseDto addComment(CommentRequestDto dto) {

        //jwt로 유저 정보 얻기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        Comment parent = null;
        if (dto.getParentId() != null) {
            parent = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));

            if (parent.getParent() != null) {
                throw new RuntimeException("대댓글에는 더 이상 댓글을 달 수 없습니다.");
            }
        }


        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .parent(parent)
                .content(dto.getContent())
                .build();

        if (parent != null) {
            parent.getChildren().add(comment);
        }

        Comment saved = commentRepository.save(comment);

        return toDto(saved);
    }

    private CommentResponseDto toDto(Comment comment) {
        List<CommentResponseDto> childrenDtos = comment.getChildren() == null
                ? List.of()  // null이면 빈 리스트 반환
                : comment.getChildren().stream()
                .map(this::toDto)
                .toList();

        return CommentResponseDto.builder()
                .commentId(comment.getId())
                .username(comment.getUser().getUsername())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .build();

    }
}
