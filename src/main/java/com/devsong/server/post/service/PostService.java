package com.devsong.server.post.service;

import com.devsong.server.post.dto.PostCreateRequestDto;
import com.devsong.server.post.dto.PostCreateResponseDto;
import com.devsong.server.post.dto.PostListResponseDto;
import com.devsong.server.post.entity.Post;
import com.devsong.server.post.dto.PostResponseDto;
import com.devsong.server.post.repository.PostRepository;
import com.devsong.server.user.entity.User;
import com.devsong.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;


    //게시글 등록
    @Transactional
    public PostCreateResponseDto create(PostCreateRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId()).get();

        Post post = requestDto.toEntity(user);
        postRepository.save(post);
        return new PostCreateResponseDto(post.getId());
    }


    //게시글 상세정보 조회 (없을 시 코멘트 출력)
    @Transactional(readOnly = true)
    public PostResponseDto findPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        return PostResponseDto.from(post);
    }


    //전체 게시글 목록 조회
    @Transactional(readOnly = true)
    public List<PostListResponseDto> findAll() {
        return postRepository.findAllByOrderByIdDesc().stream()
                .map(PostListResponseDto::from)
                .toList();
    }
}