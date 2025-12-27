package com.devsong.server.chat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private Long roomId; //방 번호
    private Long senderId; //보낸사람
    private String content; //내용
    private LocalDateTime createdAt; //작성시간

    public ChatMessage(Long roomId, Long senderId, String content) {
        this.roomId = roomId; //방 번호
        this.senderId = senderId; //보낸사람
        this.content = content; //내용
        this.createdAt = LocalDateTime.now(); //작성시간
    }
}

