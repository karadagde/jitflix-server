package Jitflix.Jitflix.WebSocket;

import Jitflix.Jitflix.model.data.ConnectionType;
import Jitflix.Jitflix.model.data.VideoCallSocketConnectionData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketHandler extends TextWebSocketHandler {

    private final Map<String, VideoCallSocketConnectionData> videoCallSocketConnectionDataList = new ConcurrentHashMap<>();


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {
        if (session.isOpen()) {
            String userName = session.getPrincipal().getName();
            String roomId = extractRoomId(session.getUri().toString());
            VideoCallSocketConnectionData.ParticipantWRTCData otherParticipant =
                    videoCallSocketConnectionDataList.get(roomId)
                            .getParticipants()
                            .entrySet()
                            .stream()
                            .filter(
                                    entry -> !entry.getKey().equals(userName))
                            .findFirst()
                            .get()
                            .getValue();


            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> messageMap = objectMapper.readValue(
                    message.getPayload(), Map.class);
            if (messageMap.containsKey("sdp")) {
                if (message.getPayload().contains("offer")) {
                    VideoCallSocketConnectionData.ParticipantWRTCData offer = new VideoCallSocketConnectionData.ParticipantWRTCData(
                            session, userName);
                    offer.setConnectionType(ConnectionType.OFFER);
                    offer.getSdp().add(message.getPayload());
                    otherParticipant.getSession().sendMessage(message);
                } else if (message.getPayload().contains("answer")) {
                    VideoCallSocketConnectionData.ParticipantWRTCData answer = new VideoCallSocketConnectionData.ParticipantWRTCData(
                            session, userName);
                    answer.setConnectionType(ConnectionType.ANSWER);
                    answer.getSdp().add(message.getPayload());

                    otherParticipant.getSession().sendMessage(message);
                    session.sendMessage(new TextMessage(
                            otherParticipant.getSdp().get(0))
                    );
                }
            } else if (messageMap.containsKey("candidate")) {
                VideoCallSocketConnectionData videoCallData = videoCallSocketConnectionDataList.get(
                        roomId);

                VideoCallSocketConnectionData.ParticipantWRTCData participant = videoCallData.getParticipants()
                        .get(userName);
                participant.getIceCandidates().add(message.getPayload());
                otherParticipant.getSession().sendMessage(message);
            } else {
                session.sendMessage(message);
                otherParticipant.getSession().sendMessage(message);
            }


        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws
            Exception {
        String callId = extractRoomId(String.valueOf(session.getUri()));

        VideoCallSocketConnectionData callData = new VideoCallSocketConnectionData(
                callId);

        if (!videoCallSocketConnectionDataList.containsKey(callId)) {

            videoCallSocketConnectionDataList.put(callId, callData);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus status)
            throws Exception {
        String roomId = extractRoomId(String.valueOf(session.getUri()));
        String userName = session.getPrincipal().getName();
        VideoCallSocketConnectionData videoCallData =
                videoCallSocketConnectionDataList.get(roomId);

        if (videoCallData != null) {
            VideoCallSocketConnectionData.ParticipantWRTCData participant =
                    videoCallData.getParticipants().get(userName);
            if (participant != null) {
                videoCallData.getParticipants().remove(userName);
            }
        }

    }

    private String extractRoomId(String uri) {
        return uri.substring(uri.lastIndexOf("/") + 1);
    }

}
