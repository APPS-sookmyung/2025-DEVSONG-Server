package com.devsong.server.chat.controller;

import com.devsong.server.chat.dto.ChatMessageDto;
import com.devsong.server.chat.dto.ChatMessageResponseDto;
import com.devsong.server.chat.entity.ChatMessage;
import com.devsong.server.chat.service.ChatService;
import com.devsong.server.user.entity.User;
import com.devsong.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final UserRepository userRepository;

    //메세지 전송
    @MessageMapping("/chat/send")
    public void send(ChatMessageDto dto, Principal principal) {
        if (principal == null) {
            System.out.println("Principal is NULL");
            return;
        }
        Long senderId = Long.valueOf(principal.getName());
        chatService.sendMessage(dto, senderId);
    }


    //이전 메세지 조회
    @GetMapping("/chat/rooms/{roomId}/messages")
    public List<ChatMessageResponseDto> getMessages(
            @PathVariable Long roomId,
            Authentication authentication
    ) {
        Long me = (Long) authentication.getPrincipal();
        return chatService.getMessages(roomId, me);
    }

}

