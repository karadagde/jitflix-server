package Jitflix.Jitflix.WebSocket;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final Map<String, Map<String, String>> storedMessages = new ConcurrentHashMap<>();
    private String offerMessages;
    private String answerSession;
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {
        Configuration conf = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        String sessionId = session.getId();

        // This part is for parsing the JSON message in a way that allows for null values
        // In an ideal world I may need to add some error handling here
        // TODO check if there is better way to handle object access
        DocumentContext jsonContext = JsonPath.using(conf).parse(message.getPayload());
        String sdpType = jsonContext.read("$.sdp.type", String.class);
        String sdp = jsonContext.read("$.sdp.sdp", String.class);
        String candidate = jsonContext.read("$.candidate.candidate", String.class);
        String candidateSdpMid = jsonContext.read("$.candidate.sdpMid", String.class);
        String userMessage = jsonContext.read("$.message", String.class);



        while (sdpType != null){
            if (sdpType.equals("offer")){
                this.offerMessages=sdp;
                session.sendMessage(message);
                break;
            }
            if (sdpType.equals("answer")){
                answerSession = sessionId;
                if (this.offerMessages != null){
                    session.sendMessage(new TextMessage(this.offerMessages));
                    break;
                }
            }
        }


        if(userMessage != null){
          int ses = this.sessions.indexOf(answerSession);
            if (ses != -1){
                this.sessions.get(ses).sendMessage(message);
            }
//            session.sendMessage(message);
        }


        System.out.println("message: " + message);
        System.out.println("message payload: " + message.getPayload());
        System.out.println("sessionId: " + session.getId());
        System.out.println("sdpType: " + sdpType);
        System.out.println("sdp: " + sdp);
        System.out.println("candidate: " + candidate);
        System.out.println("candidateSdpMid: " + candidateSdpMid);
        System.out.println("userMessage: " + userMessage);









        /*

        // This part is accessing messages with Map and Optional types
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Map> requestMap = objectMapper.readValue(message.getPayload(), Map.class);
        Optional<Map> sdpMap = Optional.ofNullable(requestMap.get("sdp"));

        Optional<Object> actualType2 = sdpMap.flatMap(map -> Optional.ofNullable(map.get("type")));
        actualType2.ifPresent(o -> System.out.println("actualType2: " + o));
        System.out.println("messageMap2: " + requestMap);
        */

//        if ("offer".equals(type) || "candidate".equals(type)) {
//            // Store the offer or candidate message
//            storedMessages.computeIfAbsent(sessionId, k -> new ConcurrentHashMap<>()).put(type, message.getPayload());
//        }

        for (WebSocketSession webSocketSession : sessions) {
            if (webSocketSession.isOpen() && !session.getId().equals(webSocketSession.getId())) {
                webSocketSession.sendMessage(message);
//
//                // Send any stored offer or candidate from the current session to the other sessions
////                Map<String, String> messagesForSession = storedMessages.get(sessionId);
////                if (messagesForSession != null) {
////                    for (String storedType : messagesForSession.keySet()) {
////                        String storedMessage = messagesForSession.get(storedType);
////                        webSocketSession.sendMessage(new TextMessage(storedMessage));
////                    }
////                    // Clear the stored messages after sending
////                    storedMessages.remove(sessionId);
////                }
//
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }
}
