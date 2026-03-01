package com.devsong.server.chat.service;

import com.devsong.server.chat.dto.ChatMessageDto;
import com.devsong.server.chat.dto.ChatMessageResponseDto;
import com.devsong.server.chat.entity.ChatMessage;
import com.devsong.server.chat.entity.ChatRoom;
import com.devsong.server.chat.repository.ChatMessageRepository;
import com.devsong.server.chat.repository.ChatRoomMemberRepository;
import com.devsong.server.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatReadService chatReadService;
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    public void sendMessage(ChatMessageDto dto, Long senderId) {
        if (dto.getRoomId() == null || dto.getContent() == null || dto.getContent().isBlank()) {
            throw new IllegalArgumentException("Wrong Message");
        }

        ChatRoom room = chatRoomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No room"));

        if (!chatRoomMemberRepository.isParticipant(dto.getRoomId(), senderId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized");
        }


        ChatMessage message = new ChatMessage(
                room,
                senderId,
                dto.getContent()
        );

        ChatMessage saved = chatMessageRepository.save(message);

        chatRoomService.updateRoomLastMessage(dto.getRoomId(), saved.getId());

        ChatMessageResponseDto payload = ChatMessageResponseDto.from(saved);
        String destination = "/topic/chat/room/" + dto.getRoomId();

        messagingTemplate.convertAndSend(destination, payload);
    }

    public List<ChatMessageResponseDto> getMessages(Long roomId, Long me) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "no room"));

        if (!chatRoomMemberRepository.isParticipant(roomId, me)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized");
        }

        List<ChatMessageResponseDto> list = chatMessageRepository
                .findByRoom_IdOrderByCreatedAtAsc(roomId)
                .stream()
                .map(ChatMessageResponseDto::from)
                .toList();

        //방에 들어온 시점의 메세지까지 읽음 처리
        chatReadService.markRoomAsRead(roomId, me);

        return list;
    }
}
