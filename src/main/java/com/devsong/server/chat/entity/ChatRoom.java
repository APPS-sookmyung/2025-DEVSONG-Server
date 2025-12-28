package com.devsong.server.chat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "useraid")
    private Long userAId;

    @Column(name = "userbid")
    private Long userBId;

    public ChatRoom(Long userAId, Long userBId) {
        this.userAId = userAId;
        this.userBId = userBId;
    }

}

