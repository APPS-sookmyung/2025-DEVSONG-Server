package com.devsong.server.user.controller;

import com.devsong.server.user.dto.*;
import com.devsong.server.user.entity.User;
import com.devsong.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Map;

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

    @PostMapping("/techstack")
    public ResponseEntity<UpdateTechStackResponseDto> updateTechStack(
            @RequestBody UpdateTechStackRequestDto dto
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();

        UpdateTechStackResponseDto response = userService.updateTechStack(userId, dto.getTechStack());
        return ResponseEntity.ok(response);
    }

}
