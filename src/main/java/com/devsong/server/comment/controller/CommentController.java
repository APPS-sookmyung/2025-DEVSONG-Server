package com.devsong.server.comment.controller;


import com.devsong.server.comment.dto.CommentRequestDto;
import com.devsong.server.comment.dto.CommentResponseDto;
import com.devsong.server.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public CommentResponseDto saveComment(@RequestBody CommentRequestDto dto) {
        return commentService.addComment(dto);
    }
}
