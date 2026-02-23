package com.devsong.server.chat.controller;

import com.devsong.server.chat.dto.ChatRoomCreateResponseDto;
import com.devsong.server.chat.dto.ChatRoomListResponseDto;
import com.devsong.server.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    //채팅방 조회, 없을 시 생성
    @PostMapping
    public ChatRoomCreateResponseDto createRoom(
            @RequestParam Long targetUserId, Principal principal
    ) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }
        Long myId = Long.parseLong(principal.getName());
        Long roomId = chatRoomService.getRoom(myId, targetUserId);
        return new ChatRoomCreateResponseDto(roomId);
    }

    //채팅방 목록
    @GetMapping
    public List<ChatRoomListResponseDto> myRooms(Principal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }
        Long myId = Long.parseLong(principal.getName());

        return chatRoomService.getMyRooms(myId);
    }
}

