package com.devsong.server.chat.controller;

import com.devsong.server.chat.dto.ChatRoomCreateResponseDto;
import com.devsong.server.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    //채팅방 조회, 없을 시 생성
    @PostMapping
    public ChatRoomCreateResponseDto createRoom(
            Long targetUserId,
            Principal principal
    ) {
        Long myId = Long.parseLong(principal.getName());
        Long roomId = chatRoomService.getRoom(myId, targetUserId);
        return new ChatRoomCreateResponseDto(roomId);
    }
}

