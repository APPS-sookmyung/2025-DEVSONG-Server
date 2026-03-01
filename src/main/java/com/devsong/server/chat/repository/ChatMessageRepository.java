package com.devsong.server.chat.repository;

import com.devsong.server.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository
        extends JpaRepository<ChatMessage, Long> {

    //기존 메세지 조회
    List<ChatMessage> findByRoomIdOrderByCreatedAtAsc(Long roomId);
}

