package com.devsong.server.controller;

import com.devsong.server.dto.PostListResponseDto;
import com.devsong.server.dto.PostRequestDto;
import com.devsong.server.dto.PostResponseDto;
import com.devsong.server.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public Long SavePost(@RequestBody PostRequestDto requestDto) {
        return postService.create(requestDto);
    }

    @GetMapping("/{id}")
    public PostResponseDto searchById(@PathVariable Long id) {
        return postService.findPost(id);
    }

    @GetMapping
    public List<PostListResponseDto> searchAllDesc() {
        return postService.findAll();
    }

}

