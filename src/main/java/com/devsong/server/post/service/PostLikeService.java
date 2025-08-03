package com.devsong.server.post.service;

import com.devsong.server.post.dto.PostLikeRequestDto;
import com.devsong.server.post.dto.PostLikeResponseDto;
import com.devsong.server.post.entity.Post;
import com.devsong.server.post.entity.PostLike;
import com.devsong.server.post.repository.PostLikeRepository;
import com.devsong.server.post.repository.PostRepository;
import com.devsong.server.user.entity.User;
import com.devsong.server.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public PostLikeResponseDto togglePostLike(PostLikeRequestDto dto) {
        boolean exists = postLikeRepository.existsByUserIdAndPostId(dto.getUserId(), dto.getPostId());

        if (exists) {
            postLikeRepository.deleteByUserIdAndPostId(dto.getUserId(), dto.getPostId());
            return PostLikeResponseDto.builder()
                    .postLikeId(null)
                    .userId(dto.getUserId())
                    .build();
        } else {
            Post post = postRepository.findById(dto.getPostId())
                    .orElseThrow(() -> new RuntimeException("Post not found"));

            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            PostLike postLike = PostLike.builder()
                    .user(user)
                    .post(post)
                    .build();

            postLikeRepository.save(postLike);

            return PostLikeResponseDto.builder()
                    .postLikeId(postLike.getId())
                    .userId(user.getId())
                    .build();
        }
    }
}
