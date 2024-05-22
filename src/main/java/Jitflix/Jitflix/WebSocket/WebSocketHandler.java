package Jitflix.Jitflix.WebSocket;

import Jitflix.Jitflix.model.data.ParticipantWRTCData;
import Jitflix.Jitflix.model.data.VideoCallData;
import Jitflix.Jitflix.service.videcall.WebsocketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

public class WebSocketHandler extends TextWebSocketHandler {

    private final WebsocketService websocketService;
    Logger logger = Logger.getLogger(WebSocketHandler.class.getName());

    public WebSocketHandler(WebsocketService websocketService) {
        this.websocketService = websocketService;
    }


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {
        if (session.isOpen()) {
//            Principal userPrincipal = session.getPrincipal();
//            if (userPrincipal == null) {
//                session.close(CloseStatus.NOT_ACCEPTABLE);
//                return;
//            }

//            String userName = userPrincipal.getName();
            String roomId = extractRoomId(
                    Objects.requireNonNull(session.getUri()));

            boolean isRoomValid = websocketService.isRoomValid(roomId);
            if (!isRoomValid) {
                session.close(CloseStatus.NOT_ACCEPTABLE);
                return;
            }

//            boolean isParticipantHost = websocketService.isParticipantHost(
//                    roomId,
//                    userName);
//            ParticipantWRTCData peerData = websocketService.getPeerData(roomId,
//                    userName);
            List<WebSocketSession> roomSessions =
                    websocketService.getVideoCallData(roomId).getParticipants()
                            .values().stream()
                            .map(ParticipantWRTCData::getSession)
                            .toList();

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> messageMap = objectMapper.readValue(
                    message.getPayload(), Map.class);

//            ParticipantWRTCData currentUser =
//                    websocketService.getParticipant(roomId, userName, session);
            if (messageMap.containsKey("sdp") ||
                messageMap.containsKey("candidate")) {

                Optional<WebSocketSession> peerSession = roomSessions.stream()
                        .filter(s -> !s.equals(session))
                        .findFirst();
                if (peerSession.isPresent()) {
                    peerSession.get().sendMessage(message);

                }

//                if (message.getPayload().contains("offer")) {
//                    logger.log(Level.INFO, "Offer received");
//                    logger.log(Level.INFO, message.getPayload());
//                    websocketService.setConnectionType(roomId, userName,
//                            ConnectionType.OFFER);
//                } else if (message.getPayload().contains("answer")) {
//                    websocketService.setConnectionType(roomId, userName,
//                            ConnectionType.ANSWER);
//                }
//                // here we set sdp data of the current user
//                websocketService.setSdp(roomId, userName, message.getPayload());
//
//                if (peerData != null) {
//                    if (message.getPayload().contains("answer")) {
//                        peerData.getSdp().forEach(sdp -> {
//                            try {
//                                peerData.getSession()
//                                        .sendMessage(new TextMessage(sdp));
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        });
//                    }
//                    peerData.getSession().sendMessage(message);
//                }
//            } else if (messageMap.containsKey("candidate")) {
//                currentUser.getIceCandidates().add(message.getPayload());
//                if (peerData != null) {
//                    peerData.getSession().sendMessage(message);
//                }
//            } else {
//                session.sendMessage(message);
//                if (peerData != null) {
//                    peerData.getSession().sendMessage(message);
//                }
            } else {
                for (WebSocketSession roomSession : roomSessions) {
                    roomSession.sendMessage(message);
                }
            }
        }
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws
            Exception {
        Principal userPrincipal = session.getPrincipal();
//        if (userPrincipal == null) {
//            session.close(CloseStatus.NOT_ACCEPTABLE);
//            return;
//        }
        String userName = userPrincipal.getName();
        String roomId = extractRoomId(
                Objects.requireNonNull(session.getUri()));
        boolean isRoomValid = websocketService.isRoomValid(roomId);
        if (isRoomValid) {
            websocketService.setParticipantData(roomId,
                    new ParticipantWRTCData(session, userName));
        } else {
            session.close(CloseStatus.NOT_ACCEPTABLE);
        }


    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus status)
            throws Exception {
        String roomId = extractRoomId(Objects.requireNonNull(session.getUri()));
        String userName = session.getPrincipal().getName();
        VideoCallData videoCallData = websocketService.getVideoCallData(roomId);

        if (videoCallData != null) {
            videoCallData.getParticipants().remove(userName);
            if (videoCallData.getParticipants().isEmpty()) {
                websocketService.removeVideoCallData(roomId);
            }
        }
    }

    private String extractRoomId(URI uri) {
        return uri.getPath().substring(uri.getPath().lastIndexOf("/") + 1);
    }

}
