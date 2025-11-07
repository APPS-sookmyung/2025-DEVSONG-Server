package com.devsong.server.post.controller;

import com.devsong.server.post.dto.*;
import com.devsong.server.post.entity.Category;
import com.devsong.server.post.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final PostApplyService postApplyService;
    private final PostLikeService postLikeService;


    //게시글 등록
    @PostMapping("/write")
    public ResponseEntity<PostCreateResponseDto> create(@RequestBody PostCreateRequestDto dto) {
        PostCreateResponseDto response = postService.create(dto);
        return ResponseEntity.ok(response);
    }


    //게시글 상세정보 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponseDto> getPost(@PathVariable Long postId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); //jwt에서 유저 정보 가져오기
        Long userId = (Long) auth.getPrincipal();

        PostDetailResponseDto responseDto = postService.findPost(postId, userId);
        return ResponseEntity.ok(responseDto);
    }


    //게시글 목록 조회
    @GetMapping
    public ResponseEntity<PostPageResponseDto> findAll(@RequestParam(required = false) String category,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "0") int page // 페이지 번호, 기본값 0
    ) {
        PostPageResponseDto response = postService.findPosts(category, sortBy, page);
        return ResponseEntity.ok(response);
    }

    //게시글 댓글 작성
    @PostMapping("/comment")
    public ResponseEntity<CommentResponseDto> saveComment(@RequestBody CommentRequestDto dto) {
        CommentResponseDto response = commentService.addComment(dto);
        return ResponseEntity.ok(response);
    }

    //게시글에 지원하기
    @PostMapping("/apply")
    public ResponseEntity<PostApplyResponseDto> apply(@RequestBody PostApplyRequestDto dto) {
        PostApplyResponseDto response = postApplyService.applyPost(dto);
        return ResponseEntity.ok(response);
    }

    //게시글 좋아요 누르기 및 취소하기
    @PostMapping("/like")
    public ResponseEntity<PostLikeResponseDto> toggle(@RequestBody PostLikeRequestDto dto) {
        PostLikeResponseDto response = postLikeService.togglePostLike(dto);
        return ResponseEntity.ok(response);
    }

    //지원자 목록 조회
    @GetMapping("/{id}/applicantlist")
    public ResponseEntity<List<PostApplicantListResponseDto>> getApplicants(@PathVariable("id") Long postId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();

        List<PostApplicantListResponseDto> applicants = postApplyService.getApplicants(postId, userId);
        return ResponseEntity.ok(applicants);
    }

    //게시글 마감하기
    @PostMapping("/{id}/close")
    public ResponseEntity<PostCloseResponseDto> closePost(@PathVariable Long id) {
        PostCloseResponseDto response = postService.closePost(id);
        return ResponseEntity.ok(response);
    }

    //현재 인기글 조회 (베스트 글 불러오기)
    @GetMapping("/best")
    public ResponseEntity<List<PostBestResponseDto>> getBestPosts() {
        List<PostBestResponseDto> bestPosts = postService.findBestPosts();
        return ResponseEntity.ok(bestPosts);
    }

    //게시글 수정
    @PostMapping("/update")
    public ResponseEntity<PostDetailResponseDto> updatePost(@RequestBody PostUpdateRequestDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();

        postService.updatePost(dto, userId);
        PostDetailResponseDto response = postService.findPost(dto.getPostId(), userId);

        return ResponseEntity.ok(response);
    }
}



