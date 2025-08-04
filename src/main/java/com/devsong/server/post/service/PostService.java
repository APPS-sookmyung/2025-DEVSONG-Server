package com.devsong.server.post.service;

import com.devsong.server.post.dto.*;
import com.devsong.server.post.entity.Post;
import com.devsong.server.post.repository.PostRepository;
import com.devsong.server.user.entity.User;
import com.devsong.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;


    //게시글 등록
    @Transactional
    public PostCreateResponseDto create(PostCreateRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
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
    public PostResponseDto findPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getUser().getUsername(),
                post.getContent(),
                post.getCreatedAt(),
                post.isClosed(),
                post.getLike(),
                post.getComment(),
                post.getPostCommentList().stream()
                        .map(comment -> new CommentResponseDto(
                                comment.getId(),
                                comment.getUser().getId(),
                                comment.getPost().getId(),
                                comment.getContent(),
                                comment.getCreatedAt()
                        ))
                        .toList()
                );

    }


    //전체 게시글 목록 조회
    @Transactional(readOnly = true)
    public List<PostListResponseDto> findAll() {
        return postRepository.findAllByOrderByIdDesc().stream()
                .map(post -> new PostListResponseDto(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getUser().getUsername(),
                        post.getCreatedAt(),
                        post.isClosed(),
                        post.getLike(),
                        post.getComment()
                ))
                .toList();
    }
}