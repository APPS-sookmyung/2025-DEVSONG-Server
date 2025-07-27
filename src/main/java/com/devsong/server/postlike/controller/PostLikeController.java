package com.devsong.server.postlike.controller;


import com.devsong.server.postlike.dto.PostLikeRequestDto;
import com.devsong.server.postlike.dto.PostLikeResponseDto;
import com.devsong.server.postlike.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/like")
    public PostLikeResponseDto toggle(@RequestBody PostLikeRequestDto dto) {
        return postLikeService.togglePostLike(dto);
    }
}
