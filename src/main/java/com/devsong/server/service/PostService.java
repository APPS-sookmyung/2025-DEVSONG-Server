package com.devsong.server.service;

import com.devsong.server.domain.Post;
import com.devsong.server.dto.PostListResponseDto;
import com.devsong.server.dto.PostRequestDto;
import com.devsong.server.dto.PostResponseDto;
import com.devsong.server.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public Long create(PostRequestDto requestDto) {
        Post post = requestDto.toEntity();      // 1단계: DTO → 엔티티 변환
        postRepository.save(post);              // 2단계: 저장
        return post.getId();                    // 3단계: ID 반환
    }


    @Transactional(readOnly = true)
    public PostResponseDto findPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        return new PostResponseDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostListResponseDto> findAll() {
        return postRepository.findAllByOrderByIdDesc().stream()
                .map(PostListResponseDto::new)
                .toList();
    }
}