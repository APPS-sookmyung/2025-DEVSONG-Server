package com.devsong.server.chat.service;

import com.devsong.server.chat.dto.ChatRoomListResponseDto;
import com.devsong.server.chat.entity.ChatRoom;
import com.devsong.server.chat.entity.ChatRoomMember;
import com.devsong.server.chat.repository.ChatMessageRepository;
import com.devsong.server.chat.repository.ChatRoomMemberRepository;
import com.devsong.server.chat.repository.ChatRoomRepository;
import com.devsong.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    //두 사용자간의 채팅방 존재 여부 확인, 없으면 생성
    @Transactional
    public Long getRoom(Long me, Long target) {
        if (me.equals(target)) {
            throw new IllegalArgumentException("not allowed");
            }

        long low = Math.min(me, target);
        long high = Math.max(me, target);

        //low, high 사용자 id로 채팅방 조회
        return chatRoomRepository.findByUserLowIdAndUserHighId(low, high)
                .map(ChatRoom::getId)
                .orElseGet(() -> {
                    try {
                        //없다면 채팅방 생성 (반드시 low/high 값 세팅되게 생성자 사용)
                        ChatRoom room = chatRoomRepository.save(new ChatRoom(me, target));

                        //멤버 2명 생성
                        chatRoomMemberRepository.save(new ChatRoomMember(room, me));
                        chatRoomMemberRepository.save(new ChatRoomMember(room, target));

                        return room.getId();

                    } catch (DataIntegrityViolationException e) {

                        //있으면 조회해 반환
                        return chatRoomRepository.findByUserLowIdAndUserHighId(low, high)
                                .map(ChatRoom::getId)
                                .orElseThrow(() -> e);
                    }
                });
    }

    public boolean isParticipant(Long roomId, Long userId) {
        return chatRoomMemberRepository.isParticipant(roomId, userId);

    }

    @Transactional(readOnly = true)
    public List<ChatRoomListResponseDto> getMyRooms(Long myId) {

        List<ChatRoom> rooms = chatRoomMemberRepository.findMyRooms(myId);

        return rooms.stream()
                .map(room -> {
                    Long otherId = room.getOtherUserId(myId);

                    //상대 유저 이름, 프로필사진
                    var otherUser = userRepository.findById(otherId)
                            .orElseThrow(() -> new IllegalStateException("no user: " + otherId));

                    String otherName = otherUser.getUsername();
                    String otherProfile = otherUser.getProfileImageUrl();

                    //마지막 메세지 내용
                    String lastMessageContent = null;
                    if (room.getLastMessageId() != null) {
                        lastMessageContent = chatMessageRepository.findById(room.getLastMessageId())
                                .map(m -> m.getContent())
                                .orElse(null);
                    }

                    //안 읽은 메세지 카운트
                    Long lastRead = chatRoomMemberRepository
                            .findByRoom_IdAndUserId(room.getId(), myId)
                            .map(ChatRoomMember::getLastReadMessageId)
                            .orElse(null);

                    long unreadCount;
                    if (lastRead == null) {
                        unreadCount = chatMessageRepository.countByRoom_IdAndSenderIdNot(room.getId(), myId);
                    } else {
                        unreadCount = chatMessageRepository.countByRoom_IdAndIdGreaterThanAndSenderIdNot(
                                room.getId(), lastRead, myId
                        );
                    }

                    return new ChatRoomListResponseDto(
                            room.getId(),
                            otherId,
                            otherName,
                            otherProfile,
                            lastMessageContent,
                            room.getLastMessageAt(),
                            unreadCount
                    );
                })
                .toList();
    }

    @Transactional
    public void updateRoomLastMessage(Long roomId, Long messageId) {

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalStateException("no room: " + roomId));

        var message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalStateException("no message: " + messageId));

        room.updateLastMessage(message.getId(), message.getCreatedAt());
    }

}

