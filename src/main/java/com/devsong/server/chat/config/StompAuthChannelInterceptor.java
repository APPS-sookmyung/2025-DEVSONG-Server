package com.devsong.server.chat.config;

import com.devsong.server.chat.service.ChatRoomService;
import com.devsong.server.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final ChatRoomService chatRoomService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String auth = accessor.getFirstNativeHeader("Authorization");
            if (auth == null || !auth.startsWith("Bearer ")) {
                throw new IllegalArgumentException("No JWT token");
            }

            String token = auth.substring(7);
            if (!jwtTokenProvider.validateToken(token)) {
                throw new IllegalArgumentException("Invalid JWT token");
            }

            Long userId = jwtTokenProvider.getUserId(token);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userId.toString(), null, List.of());

            accessor.setUser(authentication);
        }

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {

            Authentication auth = (Authentication) accessor.getUser();
            if (auth == null) {
                throw new IllegalArgumentException("Unauthenticated subscribe");
            }

            Long userId = Long.valueOf(auth.getName());
            String destination = accessor.getDestination();

            if (destination != null && destination.startsWith("/topic/chat/room/")) {
                Long roomId = Long.valueOf(
                        destination.substring("/topic/chat/room/".length())
                );

                if (!chatRoomService.isParticipant(roomId, userId)) {
                    throw new IllegalArgumentException("No permission to subscribe this room");
                }
            }
        }

        return message;
    }
}

