package com.devsong.server.chat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"room_id", "user_id"}))
public class ChatRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //방
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private ChatRoom room;

    //유저
    @Column(nullable = false)
    private Long userId;

    //마지막으로 읽은 메세지
    private Long lastReadMessageId;

    public ChatRoomMember(ChatRoom room, Long userId) {
        this.room = room;
        this.userId = userId;
        this.lastReadMessageId = null;
    }

    public void advanceLastReadTo(Long messageId) {
        if (messageId == null) return;
        if (this.lastReadMessageId == null || this.lastReadMessageId < messageId) {
            this.lastReadMessageId = messageId;
        }
    }
}