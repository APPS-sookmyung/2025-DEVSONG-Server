package com.devsong.server.chat.repository;

import com.devsong.server.chat.entity.ChatRoom;
import com.devsong.server.chat.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomMemberRepository
        extends JpaRepository<ChatRoomMember, Long> {

    @Query("""
                select count(m) > 0
                from ChatRoomMember m
                where m.room.id = :roomId
                  and m.userId = :userId
            """)
    boolean isParticipant(@Param("roomId") Long roomId,
                          @Param("userId") Long userId);

    @Query("""
                select m.room
                from ChatRoomMember m
                where m.userId = :me
                order by 
                    (case when m.room.lastMessageAt is null then 1 else 0 end) asc,
                    m.room.lastMessageAt desc,
                    m.room.id desc
            """)
    List<ChatRoom> findMyRooms(@Param("me") Long me);

    Optional<ChatRoomMember> findByRoom_IdAndUserId(Long roomId, Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
                update ChatRoomMember m
                set m.lastReadMessageId =
                    case
                        when m.lastReadMessageId is null then :newLastRead
                        when m.lastReadMessageId < :newLastRead then :newLastRead
                        else m.lastReadMessageId
                    end
                where m.room.id = :roomId and m.userId = :userId
            """)
    int advanceLastRead(@Param("roomId") Long roomId,
                        @Param("userId") Long userId,
                        @Param("newLastRead") Long newLastRead);
}
