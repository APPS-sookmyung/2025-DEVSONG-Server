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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PostApplyService {
    private final PostApplyRepository postApplyRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public PostApplyResponseDto applyPost(PostApplyRequestDto dto) {

        //jwt로 유저 정보 얻기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        if (post.isClosed()) {
            throw new IllegalStateException("Post closed");
        }

        PostApply postApply = PostApply.builder()
                .user(user)
                .post(post)
                .build();

        PostApply saved = postApplyRepository.save(postApply);

        return PostApplyResponseDto.builder()
                .postApplyId(saved.getId())
                .build();
    }
}
