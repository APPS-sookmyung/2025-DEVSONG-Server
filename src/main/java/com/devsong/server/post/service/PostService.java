package com.devsong.server.post.service;

import com.devsong.server.post.entity.Post;
import com.devsong.server.post.dto.PostListResponseDto;
import com.devsong.server.post.dto.PostRequestDto;
import com.devsong.server.post.dto.PostResponseDto;
import com.devsong.server.post.repository.PostRepository;
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
        Post post = requestDto.toEntity();
        postRepository.save(post);
        return post.getId();
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