package com.devsong.server.post.controller;

import com.devsong.server.post.dto.PostCreateResponseDto;
import com.devsong.server.post.dto.PostListResponseDto;
import com.devsong.server.post.dto.PostResponseDto;
import com.devsong.server.post.dto.PostCreateRequestDto;
import com.devsong.server.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;


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

}

