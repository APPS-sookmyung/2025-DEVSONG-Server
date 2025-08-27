package com.devsong.server.post.service;

import com.devsong.server.post.dto.*;
import com.devsong.server.post.entity.Category;
import com.devsong.server.post.entity.Post;
import com.devsong.server.user.entity.User;
import com.devsong.server.post.repository.*;
import com.devsong.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));

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
    public PostDetailResponseDto findPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        //Entity -> ResponseDto 변환
        return PostDetailResponseDto.builder()
                .id(post.getId())
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
                        postLikeRepository.countByPostId(post.getId())
                )
                .comment(
                        commentRepository.countByPostId(post.getId())
                )
                .comments(
                        commentRepository.findByPostId(post.getId()).stream()
                                .map(comment -> CommentResponseDto.builder()
                                        .commentId(comment.getId())
                                        .username(comment.getUser().getUsername())
                                        .content(comment.getContent())
                                        .createdAt(comment.getCreatedAt())
                                        .build()
                                ).toList()
                )
                .build();
    }


    //전체 게시글 목록 조회
    @Transactional(readOnly = true)
    public List<PostListResponseDto> findAll() {
        return postRepository.findAllByOrderByIdDesc().stream()
                .map(post -> PostListResponseDto.builder()
                                .id(post.getId())
                                .title(post.getTitle())
                                .username(post.getUser().getUsername())
                                .preview(preview(post.getContent(), 80))
                                .createdAt(post.getCreatedAt())
                                .closed(post.isClosed())
                                .like(
                                        postLikeRepository.countByPostId(post.getId())
                                )
                                .comment(
                                        commentRepository.countByPostId(post.getId())
                                )
                                .build()
                )
                .toList();
    }

    //카테고리별 게시글 목록 조회
    @Transactional(readOnly = true)
    public List<PostListResponseDto> findByCategory(String category) {
        Category categoryEnum = Category.from(category);
        return postRepository.findAllByCategoryOrderByIdDesc(categoryEnum)
                .stream()
                .map(post -> PostListResponseDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .username(post.getUser().getUsername())
                        .preview(preview(post.getContent(), 80))
                        .createdAt(post.getCreatedAt())
                        .closed(post.isClosed())
                        .like(
                                postLikeRepository.countByPostId(post.getId())
                        )
                        .comment(
                                commentRepository.countByPostId(post.getId())
                        )
                        .build()
                )
                .toList();
    }

    private String preview(String content, int limit) {
        if (content == null) return "";
        return content.length() > limit ? content.substring(0, limit) + "..." : content;
    }
}

