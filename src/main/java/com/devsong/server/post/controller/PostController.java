package com.devsong.server.post.controller;

import com.devsong.server.post.dto.*;
import com.devsong.server.post.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/{id}")
    public ResponseEntity<PostDetailResponseDto> getPost(@PathVariable Long id) {
        PostDetailResponseDto responseDto = postService.findPost(id);
        return ResponseEntity.ok(responseDto);
    }


    //전체 게시글 목록 조회
    @GetMapping("/list")
    public List<PostListResponseDto> findAll() {
        return postService.findAll();
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

}

