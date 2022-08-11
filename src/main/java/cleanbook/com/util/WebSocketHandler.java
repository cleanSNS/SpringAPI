package cleanbook.com.util;

import com.google.gson.Gson;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketHandler extends TextWebSocketHandler {

    private final Map<String,WebSocketSession> sessions = new ConcurrentHashMap<>();

    // 웹소켓 연결
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        sessions.put(sessionId, session);

        Message message = Message.builder()
                .sender(sessionId)
                .channelId("all")
                .build();
        message.newConnect();

        // 본인을 제외한 모두에게 전송
        sessions.values().forEach(s -> {
            try {
                if (!s.getId().equals(sessionId)){
                    s.sendMessage(new TextMessage(message.toString()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // 양방향 데이터 통신
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        Message myMessage = new Gson().fromJson(message.getPayload().toString(), Message.class);
        myMessage.setSender(session.getId());

        WebSocketSession receiver = sessions.get(myMessage.getChannelId());

        if (receiver != null && receiver.isOpen()) {
            receiver.sendMessage(new TextMessage(myMessage.toString()));
        }
    }

    // 소켓 통신 에러
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    // 소켓 통신 종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String sessionId = session.getId();

        sessions.remove(sessionId);

        Message message = new Message();
        message.closeConnect();
        message.setSender(sessionId);

        sessions.values().forEach(s -> {
            try{
                s.sendMessage(new TextMessage(message.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
