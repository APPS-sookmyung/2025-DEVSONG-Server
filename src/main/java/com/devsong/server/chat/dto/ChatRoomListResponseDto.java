package com.devsong.server.chat.dto;

import java.time.LocalDateTime;

public record ChatRoomListResponseDto(
        Long roomId, //채팅방 id
        Long otherUserId, //상대 id
        String otherName, //상대 이름
        String otherProfileImageUrl, //상대 프로필
        String lastMessageContent, //마지막 메세지
        LocalDateTime lastMessageAt, //마지막 메세지 전송시간
        long unreadCount //안 읽은 메세지 카운트
) {}