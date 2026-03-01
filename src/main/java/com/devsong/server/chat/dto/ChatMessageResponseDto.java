package com.devsong.server.chat.dto;

import com.devsong.server.chat.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatMessageResponseDto {

    private Long messageId;
    private Long senderId;
    private String content;
    private LocalDateTime createdAt;

    public static ChatMessageResponseDto from(ChatMessage message) {
        return new ChatMessageResponseDto(
                message.getId(), //pk
                message.getSenderId(), //보낸사람
                message.getContent(), //내용
                message.getCreatedAt() //작성시간
        );
    }
}
