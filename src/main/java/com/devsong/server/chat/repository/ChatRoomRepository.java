package com.devsong.server.chat.repository;

import com.devsong.server.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByUserLowIdAndUserHighId(Long userLowId, Long userHighId);
}

