package com.devsong.server.post.controller;

import com.devsong.server.post.dto.CommentRequestDto;
import com.devsong.server.post.dto.CommentResponseDto;
import com.devsong.server.post.service.CommentService;
import com.devsong.server.post.dto.PostCreateResponseDto;
import com.devsong.server.post.dto.PostListResponseDto;
import com.devsong.server.post.dto.PostResponseDto;
import com.devsong.server.post.dto.PostCreateRequestDto;
import com.devsong.server.post.service.PostService;
import com.devsong.server.post.dto.PostApplyRequestDto;
import com.devsong.server.post.dto.PostApplyResponseDto;
import com.devsong.server.post.service.PostApplyService;
import com.devsong.server.post.dto.PostLikeRequestDto;
import com.devsong.server.post.dto.PostLikeResponseDto;
import com.devsong.server.post.service.PostLikeService;
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
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long id) {
        PostResponseDto responseDto = postService.findPost(id);
        return ResponseEntity.ok(responseDto);
    }


    //전체 게시글 목록 조회
    @GetMapping("/list")
    public List<PostListResponseDto> findAll() {
        return postService.findAll();
    }


    @PostMapping("/comment")
    public ResponseEntity<CommentResponseDto> saveComment(@RequestBody CommentRequestDto dto) {
        CommentResponseDto response = commentService.addComment(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/apply")
    public ResponseEntity<PostApplyResponseDto> apply(@RequestBody PostApplyRequestDto dto) {
        PostApplyResponseDto response = postApplyService.applyPost(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/like")
    public ResponseEntity<PostLikeResponseDto> toggle(@RequestBody PostLikeRequestDto dto) {
        PostLikeResponseDto response = postLikeService.togglePostLike(dto);
        return ResponseEntity.ok(response);
    }

}

