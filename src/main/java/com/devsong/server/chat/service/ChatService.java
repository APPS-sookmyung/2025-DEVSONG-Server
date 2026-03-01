package com.devsong.server.chat.service;

import com.devsong.server.chat.dto.ChatMessageDto;
import com.devsong.server.chat.dto.ChatMessageResponseDto;
import com.devsong.server.chat.entity.ChatMessage;
import com.devsong.server.chat.entity.ChatRoom;
import com.devsong.server.chat.repository.ChatMessageRepository;
import com.devsong.server.chat.repository.ChatRoomRepository;
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

    public void sendMessage(ChatMessageDto dto, Long senderId) {
        if (dto.getRoomId() == null || dto.getContent() == null || dto.getContent().isBlank()) {
            throw new IllegalArgumentException("Wrong Message");
        }

        ChatRoom room = chatRoomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("No room"));

        boolean isParticipant =
                chatRoomRepository.isParticipant(dto.getRoomId(), senderId);

        if (!isParticipant) {
            throw new RuntimeException("Not authorized");
        }

        ChatMessage message = new ChatMessage(
                dto.getRoomId(),
                senderId,
                dto.getContent()
        );

        ChatMessage saved = chatMessageRepository.save(message);

        ChatMessageResponseDto payload = ChatMessageResponseDto.from(saved);
        String destination = "/topic/chat/room/" + dto.getRoomId();

        messagingTemplate.convertAndSend(destination, payload);
    }

    public List<ChatMessageResponseDto> getMessages(Long roomId, Long me) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("no room"));

        boolean isParticipant =
                chatRoomRepository.isParticipant(roomId, me);

        if (!isParticipant) {
            throw new RuntimeException("Not authorized");
        }

        return chatMessageRepository
                .findByRoomIdOrderByCreatedAtAsc(roomId)
                .stream()
                .map(ChatMessageResponseDto::from)
                .toList();
    }
}
