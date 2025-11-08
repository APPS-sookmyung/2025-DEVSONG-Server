package com.devsong.server.user.controller;

import com.devsong.server.user.dto.*;
import com.devsong.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService; //DI

    @Operation(summary = "회원가입")
    @PostMapping("/signup") //회원가입
    public SignupResponseDto signup(@RequestBody SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login") //로그인
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto);
    }

    @Operation(summary = "이메일 중복확인")
    @PostMapping("/check-email") //이메일 중복확인
    public EmailResponseDto checkEmail(@RequestBody EmailRequestDto emailRequestDto) {
        return userService.checkEmail(emailRequestDto);
    }

    @Operation(summary = "기술스택 등록")
    @PostMapping("/me/techstack")
    public ResponseEntity<UpdateTechStackResponseDto> updateTechStack(
            @RequestBody UpdateTechStackRequestDto dto
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();

        UpdateTechStackResponseDto response = userService.updateTechStack(userId, dto.getTechStack());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내가 쓴 글 조회")
    @GetMapping("/me/posts")
    public List<MyPostDto> getMyPosts(@AuthenticationPrincipal Long userId) {
        return userService.getMyPosts(userId);
    }

    @Operation(summary = "내가 쓴 댓글 조회")
    @GetMapping("/me/comments")
    public List<MyPostDto> getMyCommentedPosts(@AuthenticationPrincipal Long userId) {
        return userService.getMyCommentedPosts(userId);
    }

    @Operation(summary = "내가 좋아요 누른 글 조회")
    @GetMapping("/me/likes")
    public List<MyPostDto> getMyLikedPosts(@AuthenticationPrincipal Long userId) {
        return userService.getMyLikedPosts(userId);
    }

    @Operation(summary = "내가 지원한 글 조회")
    @GetMapping("/me/applies")
    public List<MyPostDto> getMyAppliedPosts(@AuthenticationPrincipal Long userId) {
        return userService.getMyAppliedPosts(userId);
    }
}