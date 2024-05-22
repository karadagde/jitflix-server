package Jitflix.Jitflix.config.webSocket;

import Jitflix.Jitflix.WebSocket.WebSocketHandler;
import Jitflix.Jitflix.service.videcall.WebsocketService;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker

public class WebSocketConfig
        implements WebSocketConfigurer, WebSocketMessageBrokerConfigurer {


    private final WebsocketService websocketService;

    public WebSocketConfig(WebsocketService websocketService) {
        this.websocketService = websocketService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler(websocketService),
                        "/video-call/{room-id}")
                .setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

//    @Override
//    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
//        registration.setMessageSizeLimit(1024 * 1024);
//    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/test-endpoint")
                .setAllowedOrigins("*");
//                .withSockJS();
    }


}
