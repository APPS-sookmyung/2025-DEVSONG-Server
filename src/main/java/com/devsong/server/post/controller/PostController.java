package com.devsong.server.post.controller;

import com.devsong.server.post.dto.PostListResponseDto;
import com.devsong.server.post.dto.PostRequestDto;
import com.devsong.server.post.dto.PostResponseDto;
import com.devsong.server.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public Long create(@RequestBody PostRequestDto requestDto) {
        return postService.create(requestDto);
    }

    @GetMapping("/{id}")
    public PostResponseDto findPost(@PathVariable Long id) {
        return postService.findPost(id);
    }

    @GetMapping
    public List<PostListResponseDto> findAll() {
        return postService.findAll();
    }

}

