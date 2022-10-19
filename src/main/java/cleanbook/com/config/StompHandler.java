package cleanbook.com.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private WebSocketMessageBrokerStats webSocketMessageBrokerStats;

    @Override
    public void postSend(Message message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String sessionId = accessor.getSessionId();
        switch (accessor.getCommand()) {
            case CONNECT:
                log.info("WebSocket Connect");
                log.info(webSocketMessageBrokerStats.toString());
                break;
            case DISCONNECT:
                // 유저가 Websocket으로 disconnect() 를 한 뒤 호출됨 or 세션이 끊어졌을 때 발생함(페이지 이동~ 브라우저 닫기 등)
                log.info("WebSocket Disconnect");
                log.info(webSocketMessageBrokerStats.toString());
                break;
            default:
                break;
        }

    }
}