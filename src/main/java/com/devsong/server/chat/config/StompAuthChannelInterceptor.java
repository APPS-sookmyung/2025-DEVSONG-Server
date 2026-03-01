package com.devsong.server.chat.config;

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

            // 1️⃣ Authorization 헤더에서 먼저 시도
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                log.info("[STOMP][CONNECT] token from Authorization header");
            }

            // 2️⃣ 없으면 URL 파라미터(token)에서 시도 (SockJS)
            if (token == null) {
                Map<String, Object> sessionAttrs = accessor.getSessionAttributes();
                if (sessionAttrs != null && sessionAttrs.get("token") != null) {
                    token = sessionAttrs.get("token").toString();
                    log.info("[STOMP][CONNECT] token from URL parameter");
                }
            }

            // 3️⃣ 토큰 없으면 즉시 차단
            if (token == null) {
                log.error("[STOMP][CONNECT] No JWT token found");
                throw new IllegalArgumentException("No JWT token");
            }

            // 4️⃣ 토큰 검증
            if (!jwtTokenProvider.validateToken(token)) {
                log.error("[STOMP][CONNECT] Invalid JWT token");
                throw new IllegalArgumentException("Invalid JWT token");
            }

            // 5️⃣ 사용자 식별
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

        return message;
    }
}
