package com.devsong.server.user.service;

import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import com.devsong.server.user.entity.User;
import com.devsong.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class S3Service {

    private final S3Template s3Template;
    private final UserRepository userRepository;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucket;

    // 허용할 파일 형식
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("image/jpeg", "image/jpg", "image/webp", "image/png", "image/gif");
    // 5MB 제한 (byte 단위)
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    public String upload(Long userId, MultipartFile file) {
        // 파일 검증
        validateFile(file);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 1. 기존 이미지가 있다면 삭제
        if (user.getProfileS3Key() != null) {
            s3Template.deleteObject(bucket, user.getProfileS3Key());
        }

        // 2. 새 이미지 업로드
        String originalName = file.getOriginalFilename();
        String s3FileName = "profiles/" + UUID.randomUUID().toString() + "-" + originalName;

        try (InputStream inputStream = file.getInputStream()) {
            S3Resource resource = s3Template.upload(bucket, s3FileName, inputStream);
            String imageUrl = resource.getURL().toString();
            user.updateProfileImage(imageUrl, s3FileName);
            return imageUrl;
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 실패", e);
        }
    }

    // [추가된 로직] 파일 검증 메서드
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("파일이 비어있습니다.");
        }

        // 1. 파일 크기 검증
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("파일 크기는 5MB를 초과할 수 없습니다.");
        }

        // 2. 파일 형식(MIME Type) 검증
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_EXTENSIONS.contains(contentType.toLowerCase())) {
            throw new RuntimeException("허용되지 않는 파일 형식입니다. (jpeg, jpg, webp, png, gif만 가능)");
        }
    }

    public void delete(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (user.getProfileS3Key() != null) {
            s3Template.deleteObject(bucket, user.getProfileS3Key());
            user.deleteProfileImage();
        }
    }
}