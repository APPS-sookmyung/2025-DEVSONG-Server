package com.devsong.server.post.service;

import com.devsong.server.post.dto.PostApplyRequestDto;
import com.devsong.server.post.dto.PostApplyResponseDto;
import com.devsong.server.post.entity.Post;
import com.devsong.server.post.entity.PostApply;
import com.devsong.server.post.repository.PostApplyRepository;
import com.devsong.server.post.repository.PostRepository;
import com.devsong.server.user.entity.User;
import com.devsong.server.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostApplyService {
    private final PostApplyRepository postApplyRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public PostApplyResponseDto applyPost(PostApplyRequestDto dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        PostApply postApply = PostApply.builder()
                .user(user)
                .post(post)
                .build();

        PostApply saved = postApplyRepository.save(postApply);

        return PostApplyResponseDto.builder()
                .postApplyId(saved.getId())
                .userId(user.getId())
                .build();
    }
}
