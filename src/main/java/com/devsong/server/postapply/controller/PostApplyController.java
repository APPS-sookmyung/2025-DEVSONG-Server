package com.devsong.server.postapply.controller;

import com.devsong.server.postapply.dto.PostApplyRequestDto;
import com.devsong.server.postapply.dto.PostApplyResponseDto;
import com.devsong.server.postapply.service.PostApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostApplyController {

    private final PostApplyService postApplyService;

    @PostMapping("/apply")
    public PostApplyResponseDto apply(@RequestBody PostApplyRequestDto dto) {
        return postApplyService.applyPost(dto);
    }
}
