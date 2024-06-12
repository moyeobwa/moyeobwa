package momo.app.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import momo.app.auth.jwt.service.JwtCreateAndUpdateService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class WebSocketChannelInterceptor implements ChannelInterceptor {

    private final JwtCreateAndUpdateService jwtCreateAndUpdateService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT == accessor.getCommand()) {
            String accessToken = accessor.getFirstNativeHeader("Authorization");
            accessToken = accessToken.substring(7);
            try {
                jwtCreateAndUpdateService.isTokenValid(accessToken);
            } catch (Exception e) {
                log.error("failed to authorize {}", e.getMessage());
                throw e;
            }
        }
        return message;
    }
}
