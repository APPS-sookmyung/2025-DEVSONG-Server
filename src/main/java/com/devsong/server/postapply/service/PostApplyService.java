package com.devsong.server.postapply.service;

import com.devsong.server.postapply.dto.PostApplyRequestDto;
import com.devsong.server.postapply.dto.PostApplyResponseDto;
import com.devsong.server.postapply.entity.PostApply;
import com.devsong.server.postapply.repository.PostApplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostApplyService {
    private final PostApplyRepository postApplyRepository;

    @Transactional
    public PostApplyResponseDto applyPost(PostApplyRequestDto dto) {
        PostApply postApply = PostApply.builder()
                .userId(dto.getUserId())
                .postId(dto.getPostId())
                .build();

        postApplyRepository.save(postApply);

        Long id = postApplyRepository.findByPostId(postApply.getPostId()).getId();

        return new PostApplyResponseDto(id);
    }
}
