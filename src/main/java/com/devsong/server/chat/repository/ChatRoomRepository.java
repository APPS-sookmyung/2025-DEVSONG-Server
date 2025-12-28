package com.devsong.server.chat.repository;

import com.devsong.server.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    //두 사용자간 채팅방이 존재하는지 확인
    @Query("""
    select cr from ChatRoom cr
    where (cr.userAId = :a and cr.userBId = :b)
    or (cr.userAId = :b and cr.userBId = :a)
    """)
    Optional<ChatRoom> findBetweenUsers(@Param("a") Long a, @Param("b") Long b);

    @Query("""
    select count(cr) > 0
    from ChatRoom cr
    where cr.id = :roomId
      and (cr.userAId = :userId or cr.userBId = :userId)
""")
    boolean isParticipant(@Param("roomId") Long roomId,
                          @Param("userId") Long userId);
}

