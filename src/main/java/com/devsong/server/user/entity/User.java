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

}
