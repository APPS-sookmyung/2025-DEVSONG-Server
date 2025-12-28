package com.devsong.server.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //pk

    @Column(nullable = false) //이메일
    private String email;

    @Column(nullable = false)
    private String password; //비밀번호

    @Column(nullable = false)
    private String username; //실명

    @Column(nullable = false)
    private Long studentId; //학번

    @Column(nullable = false)
    private String major; //전공

    @Column(nullable = true)
    private String bojId; //백준 아이디

    @Column(nullable = true)
    private String githubId; //깃허브 아이디

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable
    private List<TechStack> techStack; //기술스택

    //백준 아이디 수정
    public void updateBojId(String bojId) { this.bojId = bojId; }

    //깃허브 아이디 수정
    public void updateGithubId(String githubId) { this.githubId = githubId; }

    //기술스택 수정
    public void updateTechStack(List<TechStack> techStack) {
        this.techStack = techStack;
    }

    // 백준 랭킹 정보
    private int bojRating = 0; // DB에 저장될 레이팅
    private int bojSolvedCount = 0; // DB에 저장될 푼 문제 수

    public void updateBojInfo(int rating, int solvedCount) {
        this.bojRating = rating;
        this.bojSolvedCount = solvedCount;
    }

    // 깃허브 랭킹 정보
    private int githubRating = 0; // DB에 저장될 레이팅
    private int commitCount = 0; // DB에 저장될 커밋 수

    public void updateGithubInfo(int rating, int solvedCount) {
        this.githubRating = rating;
        this.commitCount = solvedCount;
    }

    //프로필 사진 정보
    @Column(nullable = true)
    private String profileImageUrl; // public URL

    @Column(nullable = true)
    private String profileS3Key;    // S3에서 파일명

    // 프로필 이미지 업데이트 시 호출할 메서드
    public void updateProfileImage(String url, String key) {
        this.profileImageUrl = url;
        this.profileS3Key = key;
    }

    // 프로필 이미지 제거 시 호출
    public void deleteProfileImage() {
        this.profileImageUrl = null;
        this.profileS3Key = null;
    }
}
