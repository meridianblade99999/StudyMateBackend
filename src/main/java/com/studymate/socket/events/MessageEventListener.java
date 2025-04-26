package com.studymate.socket.events;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.studymate.socket.SocketEventListener;
import com.studymate.socket.model.SocketMessage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class MessageEventListener extends SocketEventListener<SocketMessage> {

    public MessageEventListener(SocketIOServer server) {
        super(server);
    }

    @Override
    protected String getName() {
        return "chat";
    }

    @Override
    public void onData(SocketIOClient socketIOClient, SocketMessage message, AckRequest ackRequest) throws Exception {
    }

}
