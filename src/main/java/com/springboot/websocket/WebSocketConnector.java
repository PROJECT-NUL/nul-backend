package com.springboot.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class WebSocketConnector {
    private final WebSocketHandler webSocketHandler;

    public WebSocketSession connect() throws ExecutionException, InterruptedException {
        String url = "ws://localhost:8080/websocket";
        WebSocketClient client = new StandardWebSocketClient();
        return client.execute(webSocketHandler, url).get();
    }
}
