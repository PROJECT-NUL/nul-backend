package com.springboot.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        StringBuilder buffer = (StringBuilder) session.getAttributes().computeIfAbsent("buffer", key -> new StringBuilder());

        if (message.isLast()) {
            buffer.append(message.getPayload());
            // send Message
            buffer.setLength(0);
        } else {
            buffer.append(message.getPayload());
        }
    }

    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

    }

    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        session.getAttributes().remove("buffer");
    }

    public boolean supportsPartialMessages() {
        return true;
    }
}