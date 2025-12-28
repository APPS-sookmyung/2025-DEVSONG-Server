package com.devsong.server.chat.service;

import com.devsong.server.chat.entity.ChatRoom;
import com.devsong.server.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    //두 사용자간의 채팅방 존재 여부 확인, 없으면 생성
    @Transactional
    public Long getRoom(Long me, Long target) {
        if (me.equals(target)) {
            throw new IllegalArgumentException("Self-chat is not allowed");
            }

        return chatRoomRepository
                .findBetweenUsers(me, target)
                .orElseGet(() -> chatRoomRepository.save( new ChatRoom(me, target) ))
                .getId();
    }

    public boolean isParticipant(Long roomId, Long userId) {
        return chatRoomRepository.isParticipant(roomId, userId);
    }

}

