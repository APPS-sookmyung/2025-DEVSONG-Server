package com.devsong.server.chat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_low_id", "user_high_id"}))
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userLowId;

    @Column(nullable = false)
    private Long userHighId;

    private LocalDateTime createdAt;

    private LocalDateTime lastMessageAt;

    private Long lastMessageId;

    public ChatRoom(Long userA, Long userB) {
        if (userA.equals(userB)) {
            throw new IllegalArgumentException("Cannot create chat room with self");
            }
        long low = Math.min(userA, userB);
        long high = Math.max(userA, userB);
        this.userLowId = low;
        this.userHighId = high;
        this.createdAt = LocalDateTime.now();
        this.lastMessageAt = null;
        this.lastMessageId = null;
    }

    public void updateLastMessage(Long messageId, LocalDateTime messageTime) {
        this.lastMessageId = messageId;
        this.lastMessageAt = messageTime;
    }

    //상대방 id를 빠르게 계산
    public Long getOtherUserId(Long me) {
        if (me.equals(userLowId)) return userHighId;
        if (me.equals(userHighId)) return userLowId;
        return null; //참가자가 아닌 경우
    }
}



