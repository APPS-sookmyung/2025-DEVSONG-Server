package com.devsong.server.chat.config;

import com.devsong.server.chat.service.ChatRoomService;
import com.devsong.server.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final ChatRoomService chatRoomService;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }

        StompCommand command = accessor.getCommand();
        log.info("[STOMP] command = {}", command);

        if (StompCommand.CONNECT.equals(command)) {

            String token = null;

            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                log.info("[STOMP][CONNECT] token from Authorization header");
            }


            if (token == null) {
                Map<String, Object> sessionAttrs = accessor.getSessionAttributes();
                if (sessionAttrs != null && sessionAttrs.get("token") != null) {
                    token = sessionAttrs.get("token").toString();
                    log.info("[STOMP][CONNECT] token from URL parameter");
                }
            }

            if (token == null) {
                log.error("[STOMP][CONNECT] No JWT token found");
                throw new IllegalArgumentException("No JWT token");
            }

            if (!jwtTokenProvider.validateToken(token)) {
                log.error("[STOMP][CONNECT] Invalid JWT token");
                throw new IllegalArgumentException("Invalid JWT token");
            }

            Long userId = jwtTokenProvider.getUserId(token);
            log.info("[STOMP][CONNECT] authenticated userId = {}", userId);

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            userId.toString(),
                            null,
                            List.of()
                    );

            accessor.setUser(authentication);
            log.info("[STOMP][CONNECT] Authentication set successfully");
        }

        //승인한 참가자 외 채팅방 접근 차단
        if (StompCommand.SUBSCRIBE.equals(command)) {

            if (accessor.getUser() == null) {
                log.error("[STOMP][SUBSCRIBE] No authenticated user");
                throw new IllegalArgumentException("No authenticated");
            }

            Long userId = Long.valueOf(accessor.getUser().getName());
            String dest = accessor.getDestination();
            log.info("[STOMP][SUBSCRIBE] userId={}, destination={}", userId, dest);

            if (dest == null) {
                throw new IllegalArgumentException("No destination");
            }

            String prefix = "/topic/chat/room/";
            if (dest.startsWith(prefix)) {

                String roomIdStr = dest.substring(prefix.length());
                Long roomId;

                try {
                    roomId = Long.valueOf(roomIdStr);
                } catch (NumberFormatException e) {
                    log.error("[STOMP][SUBSCRIBE] Invalid roomId in destination: {}", dest);
                    throw new IllegalArgumentException("Invalid destination");
                }

                boolean ok = chatRoomService.isParticipant(roomId, userId);
                if (!ok) {
                    log.error("[STOMP][SUBSCRIBE] Not authorized. userId={}, roomId={}", userId, roomId);
                    throw new IllegalArgumentException("Not authorized");
                }

                log.info("[STOMP][SUBSCRIBE] Authorized. userId={}, roomId={}", userId, roomId);
            }
        }

        return message;
    }
}
