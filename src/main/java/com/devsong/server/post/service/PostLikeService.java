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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public PostLikeResponseDto togglePostLike(PostLikeRequestDto dto) {

        //jwt로 유저 정보 얻기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        boolean exists = postLikeRepository.existsByUserIdAndPostId(userId, dto.getPostId());

        if (exists) {
            postLikeRepository.deleteByUserIdAndPostId(userId, dto.getPostId());
            return PostLikeResponseDto.builder()
                    .postLikeId(null)
                    .build();
        } else {
            Post post = postRepository.findById(dto.getPostId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

            PostLike postLike = PostLike.builder()
                    .user(user)
                    .post(post)
                    .build();

            postLikeRepository.save(postLike);

            return PostLikeResponseDto.builder()
                    .postLikeId(postLike.getId())
                    .build();
        }
    }
}
