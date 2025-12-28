package com.devsong.server.chat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //pk
    
    @JoinColumn(name = "room_id", nullable = false)
    private Long roomId; //방

    @JoinColumn(name = "sender_id", nullable = false)
    private Long senderId; //보낸사람

    @Column(nullable = false, length = 2000)
    private String content; //내용

    private LocalDateTime createdAt; //작성시간

    public ChatMessage(Long roomId, Long senderId, String content) {
        this.roomId = roomId; //방
        this.senderId = senderId; //보낸사람
        this.content = content; //내용
        this.createdAt = LocalDateTime.now(); //작성시간
    }
}

