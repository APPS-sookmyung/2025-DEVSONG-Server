package com.devsong.server.post.service;

import com.devsong.server.post.dto.*;
import com.devsong.server.post.entity.Category;
import com.devsong.server.post.entity.Post;
import com.devsong.server.user.entity.User;
import com.devsong.server.post.entity.Comment;
import com.devsong.server.post.repository.*;
import com.devsong.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final PostApplyRepository postApplyRepository;

    //게시글 등록
    @Transactional
    public PostCreateResponseDto create(PostCreateRequestDto requestDto) {

        //jwt로 유저 정보 얻기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        //RequestDto -> Entity 변환
        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .category(requestDto.getCategory())
                .closed(false) //마감여부 : dafualt 값을 false로
                .user(user)
                .build();

        postRepository.save(post);
        return new PostCreateResponseDto(post.getId());
    }


    //게시글 상세정보 조회 (없을 시 코멘트 출력)
    @Transactional(readOnly = true)
    public PostDetailResponseDto findPost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        List<Comment> comments = commentRepository.findByPostId(postId);

        List<CommentResponseDto> commentDtos = comments.stream()
                .filter(c -> c.getParent() == null)
                .map(c -> toDto(c, comments))
                .toList();

        //Entity -> ResponseDto 변환
        return PostDetailResponseDto.builder()
                .author(post.getUser().getId().equals(userId))
                .liked(postLikeRepository.existsByUserIdAndPostId(userId, postId))
                .applied(postApplyRepository.existsByPostIdAndUserId(postId, userId))
                .id(postId)
                .title(post.getTitle())
                .username(post.getUser().getUsername())
                .content(post.getContent())
                .category(post.getCategory())
                .major(post.getUser().getMajor())
                .studentId(post.getUser().getStudentId())
                .createdAt(post.getCreatedAt())
                .applyCount(postApplyRepository.countByPost(post))
                .closed(post.isClosed())
                .like(
                        postLikeRepository.countByPostId(postId)
                )
                .comment(
                        commentRepository.countByPostId(postId)
                )
                .comments(commentDtos)
                .build();
    }


    //전체 게시글 목록 조회
    @Transactional(readOnly = true)
    public Page<PostListResponseDto> findAll(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(post -> PostListResponseDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .username(post.getUser().getUsername())
                        .category(post.getCategory().toString())
                        .preview(preview(post.getContent(), 80))
                        .createdAt(post.getCreatedAt())
                        .closed(post.isClosed())
                        .like(postLikeRepository.countByPostId(post.getId()))
                        .comment(commentRepository.countByPostId(post.getId()))
                        .build()
                );
    }

    //카테고리별 게시글 목록 조회
    @Transactional(readOnly = true)
    public Page<PostListResponseDto> findByCategory(String category, Pageable pageable) {
        Category categoryEnum = Category.from(category);
        return postRepository.findAllByCategory(categoryEnum, pageable)
                .map(post -> PostListResponseDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .username(post.getUser().getUsername())
                        .category(post.getCategory().toString())
                        .preview(preview(post.getContent(), 80))
                        .createdAt(post.getCreatedAt())
                        .closed(post.isClosed())
                        .like(postLikeRepository.countByPostId(post.getId()))
                        .comment(commentRepository.countByPostId(post.getId()))
                        .build()
                );
    }

    private String preview(String content, int limit) {
        if (content == null) return "";
        return content.length() > limit ? content.substring(0, limit) + "..." : content;
    }

    private CommentResponseDto toDto(Comment comment, List<Comment> allComments) {
        // 현재 댓글의 자식 찾기
        List<CommentResponseDto> children = allComments.stream()
                .filter(c -> c.getParent() != null && c.getParent().getId().equals(comment.getId()))
                .map(c -> toDto(c, allComments)) // 재귀 호출
                .toList();

        return CommentResponseDto.builder()
                .commentId(comment.getId())
                .username(comment.getUser().getUsername())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .children(children.isEmpty() ? null : children)
                .build();
    }


    //게시글 마감
    @Transactional
    public PostCloseResponseDto closePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (post.isClosed()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Post already closed");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthenticated");
        }

        Object principal = auth.getPrincipal();
        Long userId;

        if (principal instanceof Long l) {
            userId = l;
        } else if (principal instanceof org.springframework.security.core.userdetails.UserDetails ud) {
            userId = Long.parseLong(ud.getUsername());
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid principal");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to close this post");
        }

        post.setClosed(true);

        return PostCloseResponseDto.builder()
                .username(user.getUsername())
                .closed(true)
                .build();
    }

}

