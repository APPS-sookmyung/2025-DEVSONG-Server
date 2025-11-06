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


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService; //DI

    @PostMapping("/signup") //회원가입
    public SignupResponseDto signup(@RequestBody SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }

    @PostMapping("/login") //로그인
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto);
    }

    @PostMapping("/check-email") //이메일 중복확인
    public EmailResponseDto checkEmail(@RequestBody EmailRequestDto emailRequestDto) {
        return userService.checkEmail(emailRequestDto);
    }

    @PostMapping("/me/techstack")
    public ResponseEntity<UpdateTechStackResponseDto> updateTechStack(
            @RequestBody UpdateTechStackRequestDto dto
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();

        UpdateTechStackResponseDto response = userService.updateTechStack(userId, dto.getTechStack());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/me/posts")
    public List<MyPostDto> getMyPosts(@AuthenticationPrincipal Long userId) {
        return userService.getMyPosts(userId);
    }

    @GetMapping("/me/comments")
    public List<MyPostDto> getMyCommentedPosts(@AuthenticationPrincipal Long userId) {
        return userService.getMyCommentedPosts(userId);
    }

    @GetMapping("/me/likes")
    public List<MyPostDto> getMyLikedPosts(@AuthenticationPrincipal Long userId) {
        return userService.getMyLikedPosts(userId);
    }

    @GetMapping("/me/applies")
    public List<MyPostDto> getMyAppliedPosts(@AuthenticationPrincipal Long userId) {
        return userService.getMyAppliedPosts(userId);
    }
}
