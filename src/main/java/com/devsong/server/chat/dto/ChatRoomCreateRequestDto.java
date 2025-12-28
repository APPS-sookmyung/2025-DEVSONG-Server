package com.devsong.server.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomCreateRequestDto {
    private Long targetUserId;
}
