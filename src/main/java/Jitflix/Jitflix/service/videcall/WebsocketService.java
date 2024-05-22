package Jitflix.Jitflix.service.videcall;import Jitflix.Jitflix.model.data.ParticipantWRTCData;import Jitflix.Jitflix.model.data.VideoCallData;import org.springframework.stereotype.Service;import org.springframework.web.socket.WebSocketSession;import java.util.Map;import java.util.concurrent.ConcurrentHashMap;@Servicepublic class WebsocketService {    private final Map<String, VideoCallData> allVideoCallData =            new ConcurrentHashMap<>();    private final VideoCallService videoCallService;    public WebsocketService(VideoCallService videoCallService) {        this.videoCallService = videoCallService;    }    public boolean isRoomValid(String roomId) {        return videoCallService.getVideoCallByRoomId(roomId) != null;    }    public boolean isParticipantHost(String roomId, String userName) {        return videoCallService.getVideoCallByRoomId(roomId)                .getCreatedBy()                .getUsername()                .equals(userName);    }    public VideoCallData getVideoCallData(String roomId) {        return allVideoCallData.computeIfAbsent(roomId, VideoCallData::new);    }    public ParticipantWRTCData getParticipant(String roomId, String userName,                                              WebSocketSession session) {        return getVideoCallData(roomId)                .getParticipants()                .computeIfAbsent(userName,                        k -> new ParticipantWRTCData(session, userName));    }//    public ParticipantWRTCData getPeerData(String roomId, String userName) {//        return getVideoCallData(roomId)//                .getParticipants()//                .entrySet()//                .stream()//                .filter(entry -> !entry.getKey().equals(userName))//                .map(Map.Entry::getValue)//                .findFirst()//                .orElse(null);//    }    public void removeParticipant(String roomId, String userName) {        getVideoCallData(roomId)                .getParticipants()                .remove(userName);    }    public void removeVideoCallData(String roomId) {        allVideoCallData.remove(roomId);    }    public void setParticipantData(String roomId,                                   ParticipantWRTCData participant) {        getVideoCallData(roomId)                .getParticipants()                .put(participant.getUserName(), participant);    }//    public void setConnectionType(String roomId, String userName,//                                  ConnectionType connectionType) {//        getVideoCallData(roomId)//                .getParticipants()//                .get(userName)//                .setConnectionType(connectionType);//    }////    public void setSdp(String roomId, String userName, String sdp) {//        getVideoCallData(roomId)//                .getParticipants()//                .get(userName)//                .getSdp()//                .add(sdp);//    }//    public void setIceCandidate(String roomId, String userName,//                                String iceCandidate) {//        getVideoCallData(roomId)//                .getParticipants()//                .get(userName)//                .getIceCandidates()//                .add(iceCandidate);//    }}