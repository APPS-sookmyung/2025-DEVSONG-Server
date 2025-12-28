package com.devsong.server.chat.service;

import com.devsong.server.chat.dto.ChatMessageDto;
import com.devsong.server.chat.dto.ChatMessageResponseDto;
import com.devsong.server.chat.entity.ChatMessage;
import com.devsong.server.chat.entity.ChatRoom;
import com.devsong.server.chat.repository.ChatMessageRepository;
import com.devsong.server.chat.repository.ChatRoomRepository;
import com.devsong.server.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;


    //메세지 전송
    public void sendMessage(ChatMessageDto dto, Long senderId) {

        if (dto.getRoomId() == null || dto.getContent() == null || dto.getContent().isBlank()) {
            throw new IllegalArgumentException("Wrong Message");
        }

        ChatRoom room = chatRoomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("No room"));

        if (!chatRoomRepository.isParticipant(dto.getRoomId(), senderId)) {
            throw new RuntimeException("Not authorized");
        }

        //DB에 저장
        ChatMessage message = new ChatMessage(
                dto.getRoomId(),
                senderId,
                dto.getContent()
        );
        ChatMessage saved = chatMessageRepository.save(message);

        //전송
        ChatMessageResponseDto payload = ChatMessageResponseDto.from(saved);
        messagingTemplate.convertAndSend("/topic/chat/room/" + dto.getRoomId(), payload);
    }

    //기존 메세지 조회
    public List<ChatMessageResponseDto> getMessages(Long roomId, Long me) {

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("no room"));

        if (!chatRoomRepository.isParticipant(roomId, me)) {
            throw new RuntimeException("Not authorized");
        }

        return chatMessageRepository
                .findByRoomIdOrderByCreatedAtAsc(roomId)
                .stream()
                .map(ChatMessageResponseDto::from)
                .toList();
    }

}

