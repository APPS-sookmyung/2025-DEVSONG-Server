package com.devsong.server.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerification {

    @Id
    private String email; // 이메일을 PK로 사용
    private String code;
    private LocalDateTime expirationTime; // 만료 시간
    private int attemptCount = 0; // 인증 시도 횟수
    private static final int MAX_ATTEMPTS = 5; // 5번으로 제한

    // 인증번호 업데이트를 위한 편의 메서드
    public void updateCode(String code, int durationMinutes) {
        this.code = code;
        this.expirationTime = LocalDateTime.now().plusMinutes(durationMinutes);
    }

    // 만료 여부 확인
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expirationTime);
    }

    // 인증 시도 횟수 제한
    public boolean incrementAttemptAndCheckLimit() {
        this.attemptCount++;
        return this.attemptCount >= MAX_ATTEMPTS;
    }
}