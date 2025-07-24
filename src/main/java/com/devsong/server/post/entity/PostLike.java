package com.devsong.server.post.entity;

import com.devsong.server.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //pk

    @ManyToOne(fetch = FetchType.LAZY)
    private User user; //유저

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post; //게시글

}

