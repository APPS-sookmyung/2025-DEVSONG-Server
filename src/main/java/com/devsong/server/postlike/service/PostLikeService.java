package com.devsong.server.postlike.service;

import com.devsong.server.postlike.dto.PostLikeRequestDto;
import com.devsong.server.postlike.dto.PostLikeResponseDto;
import com.devsong.server.postlike.entity.PostLike1;
import com.devsong.server.postlike.repository.PostLikeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;

    @Transactional
    public PostLikeResponseDto togglePostLike(PostLikeRequestDto dto) {
        boolean exists = postLikeRepository.existsByUserIdAndPostId(dto.getUserId(), dto.getPostId());

        if (exists) {
            postLikeRepository.deleteByUserIdAndPostId(dto.getUserId(), dto.getPostId());
            return new PostLikeResponseDto(null);
        } else {
            PostLike1 postLikeEntity = PostLike1.builder()
                    .userId(dto.getUserId())
                    .postId(dto.getPostId())
                    .build();

            postLikeRepository.save(postLikeEntity);
            return new PostLikeResponseDto(postLikeEntity.getId());
        }
    }
}
