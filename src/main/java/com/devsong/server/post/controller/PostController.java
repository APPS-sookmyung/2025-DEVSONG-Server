package com.devsong.server.post.controller;

import com.devsong.server.post.dto.PostListResponseDto;
import com.devsong.server.post.dto.PostRequestDto;
import com.devsong.server.post.dto.PostResponseDto;
import com.devsong.server.post.service.PostService;
import com.devsong.server.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;


    //게시글 등록
    @PostMapping("/write")
    public Long create(@RequestBody PostRequestDto requestDto,
                       @AuthenticationPrincipal User user) {
        return postService.create(requestDto, user);
    }


    //게시글 상세정보 조회
    @GetMapping("/{id}")
    public PostResponseDto findPost(@PathVariable Long id) {
        return postService.findPost(id);
    }


    //전체 게시글 목록 조회
    @GetMapping("/list")
    public List<PostListResponseDto> findAll() {
        return postService.findAll();
    }

}

