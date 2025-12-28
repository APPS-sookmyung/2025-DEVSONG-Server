package com.devsong.server.user.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.devsong.server.user.entity.User;
import com.devsong.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class S3Service {

    //DI
    private final AmazonS3 amazonS3;
    private final UserRepository userRepository;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucket;

    public String upload(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 1. 기존 이미지가 있다면 S3에서 먼저 삭제 (용량 낭비 방지)
        if (user.getProfileS3Key() != null) {
            amazonS3.deleteObject(bucket, user.getProfileS3Key());
        }

        // 2. 새 이미지 업로드
        String originalName = file.getOriginalFilename();
        String s3FileName = UUID.randomUUID().toString() + "-" + originalName;

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentType(file.getContentType());
        objMeta.setContentLength(file.getSize());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, s3FileName, inputStream, objMeta));
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 실패");
        }

        String imageUrl = amazonS3.getUrl(bucket, s3FileName).toString();

        // 3. User 엔티티 정보 업데이트
        user.updateProfileImage(imageUrl, s3FileName);

        return imageUrl;
    }

    public void delete(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Runtime Exception"));

        if (user.getProfileS3Key() != null) {
            // 1. S3에서 삭제
            amazonS3.deleteObject(bucket, user.getProfileS3Key());
            // 2. User 엔티티 필드 초기화
            user.deleteProfileImage();
        }
    }
}