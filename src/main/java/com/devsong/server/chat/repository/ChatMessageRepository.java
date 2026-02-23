package com.devsong.server.chat.repository;

import com.devsong.server.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository
        extends JpaRepository<ChatMessage, Long> {

    //기존 메시지 조회
    List<ChatMessage> findByRoom_IdOrderByCreatedAtAsc(Long roomId);

    //안 읽은 메세지 카운트
    long countByRoom_IdAndSenderIdNot(Long roomId, Long senderId);
    long countByRoom_IdAndIdGreaterThanAndSenderIdNot(Long roomId, Long lastReadMessageId, Long senderId);

    //마지막 메세지 id
    @Query("select max(m.id) from ChatMessage m where m.room.id = :roomId")
    Long findLatestMessageId(@Param("roomId") Long roomId);

}

