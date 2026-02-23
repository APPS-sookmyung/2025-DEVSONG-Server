package com.devsong.server.chat.service;

import com.devsong.server.chat.repository.ChatMessageRepository;
import com.devsong.server.chat.repository.ChatRoomMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ChatReadService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Transactional
    public void markRoomAsRead(Long roomId, Long me) {
        //채팅방 접근권한 체크
        if (!chatRoomMemberRepository.isParticipant(roomId, me)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized");
        }

        Long latestMessageId = chatMessageRepository.findLatestMessageId(roomId);
        if (latestMessageId == null) return; //메세지 없을 시

        chatRoomMemberRepository.advanceLastRead(roomId, me, latestMessageId);
    }
}