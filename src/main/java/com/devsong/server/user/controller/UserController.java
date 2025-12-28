package com.devsong.server.user.controller;

import com.devsong.server.user.dto.*;
import com.devsong.server.user.service.EmailService;
import com.devsong.server.user.service.UserService;
import com.devsong.server.user.service.ResumeService;
import com.devsong.server.user.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    //DI
    private final UserService userService;
    private final ResumeService resumeService;
    private final S3Service s3Service;
    private final EmailService emailService;

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

    @Operation(summary = "이메일로 인증번호 전송")
    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDto emailRequestDto) {
        return ResponseEntity.ok(emailService.sendEmail(emailRequestDto));
    }

    @Operation(summary = "인증번호 일치 확인")
    @PostMapping("/verify-code")
    public EmailResponseDto verifyEmail(@RequestBody EmailVerifyRequestDto emailVerifyRequestDto) {
        return emailService.verifyEmail(emailVerifyRequestDto);
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

    @Operation(summary = "이력서 조회")
    @GetMapping("/me/resume")
    public ResponseEntity<ResumeResponseDto> getMyResume(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(resumeService.getResume(userId));
    }

    @Operation(summary = "이력서 수정")
    @PostMapping("/me/resume")
    public ResponseEntity<UpdateResumeResponseDto> updateMyResume(
            @AuthenticationPrincipal Long userId,
            @RequestBody UpdateResumeRequestDto dto) {
        return ResponseEntity.ok(resumeService.updateResume(userId, dto));
    }

    @Operation(summary = "내 프로필 사진 업로드")
    @PostMapping(value = "/profile/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProfileImage(
            @AuthenticationPrincipal Long userId,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        String profileImageUrl = s3Service.upload(userId, image);
        return ResponseEntity.ok(profileImageUrl);
    }

    @Operation(summary = "내 프로필 사진 삭제")
    @PostMapping("/profile/delete")
    public ResponseEntity<String> deleteProfileImage(@AuthenticationPrincipal Long userId) {

        s3Service.delete(userId);
        return ResponseEntity.ok("Deleted profile image");
    }
}