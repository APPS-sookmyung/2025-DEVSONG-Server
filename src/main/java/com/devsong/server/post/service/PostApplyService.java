package com.devsong.server.post.service;

import com.devsong.server.post.dto.PostApplicantListResponseDto;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
        if (auth == null || auth.getPrincipal() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthenticated");
        }

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

    //지원자 목록 확인
    @Transactional
    public List<PostApplicantListResponseDto> getApplicants(Long postId, Long loginUserId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        //글 작성자가 아닐 경우 권한 없음
        if (!post.getUser().getId().equals(loginUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not Authorized");
        }

        List<PostApply> applies = postApplyRepository.findByPostId(postId);

        return applies.stream()
                .map(apply -> PostApplicantListResponseDto.builder()
                        .userId(apply.getUser().getId())
                        .username(apply.getUser().getUsername())
                        .major(apply.getUser().getMajor())
                        .studentId(apply.getUser().getStudentId())
                        .build())
                .toList();
    }
}
