package com.devsong.server.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Entity
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

}
