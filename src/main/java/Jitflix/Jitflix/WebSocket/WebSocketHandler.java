package Jitflix.Jitflix.WebSocket;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
//    private Map<String, Map<String, String>> storedMessages = new ConcurrentHashMap<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {

        String sessionId = session.getId();
////        ObjectMapper objectMapper = new ObjectMapper();
////System.out.println("message: " + message);
////System.out.println("message payload: " + message.getPayload());
////System.out.println("sessionId: " + session.getId());
//
//        Map<String, Object> messageMap = objectMapper.readValue(message.getPayload(), new TypeReference<Map<String,Object>>(){});
////        System.out.println("messageMap: " + messageMap);
//        Map<String, Object> type = (Map<String, Object>) messageMap.get("sdp");
//        if(type != null){
//            System.out.println("type: " + type.getClass());
//            Map<String, Object> messageType = (Map<String, Object>) type.get("type");
//    System.out.println("type: " + messageType);
//
//        }
//        if ("offer".equals(type) || "candidate".equals(type)) {
//            // Store the offer or candidate message
//            storedMessages.computeIfAbsent(sessionId, k -> new ConcurrentHashMap<>()).put(type, message.getPayload());
//        }

        for (WebSocketSession webSocketSession : sessions) {
            if (webSocketSession.isOpen() && !session.getId().equals(webSocketSession.getId())) {
                webSocketSession.sendMessage(message);

                // Send any stored offer or candidate from the current session to the other sessions
//                Map<String, String> messagesForSession = storedMessages.get(sessionId);
//                if (messagesForSession != null) {
//                    for (String storedType : messagesForSession.keySet()) {
//                        String storedMessage = messagesForSession.get(storedType);
//                        webSocketSession.sendMessage(new TextMessage(storedMessage));
//                    }
//                    // Clear the stored messages after sending
//                    storedMessages.remove(sessionId);
//                }

            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }
}
