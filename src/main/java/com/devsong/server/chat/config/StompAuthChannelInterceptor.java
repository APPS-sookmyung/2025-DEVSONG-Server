package com.devsong.server.chat.config;

import com.devsong.server.chat.service.ChatRoomService;
import com.devsong.server.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
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
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String token = (String) accessor.getSessionAttributes().get("token");

            if (token == null || !jwtTokenProvider.validateToken(token)) {
                throw new IllegalArgumentException("Invalid JWT token");
            }

            Long userId = jwtTokenProvider.getUserId(token);

            Authentication auth =
                    new UsernamePasswordAuthenticationToken(
                            userId.toString(),
                            null,
                            List.of()
                    );

            accessor.setUser(auth);
        }


        return message;
    }
}

