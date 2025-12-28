package com.devsong.server.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //pk

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String content; //자기소개

    private String profileImage; //프로필사진

    @Builder.Default
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "resume_interests", joinColumns = @JoinColumn(name = "resume_id"))
    @Column(name = "interest")
    private List<String> interests = new ArrayList<>(); // TechStack -> String 변경

    public void update(List<String> interests, String content, String profileImage) {
        this.interests = interests;
        this.content = content;
        this.profileImage = profileImage;
    }
}