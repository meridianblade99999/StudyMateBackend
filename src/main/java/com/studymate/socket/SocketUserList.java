package com.studymate.socket;

import com.corundumstudio.socketio.SocketIOClient;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SocketUserList {

    private final Map<Long, SocketIOClient> users = new ConcurrentHashMap<>();

    public void add(long userId, SocketIOClient socketIOClient) {
        socketIOClient.set("userId", userId);
        SocketIOClient oldSocket = users.put(userId, socketIOClient);
        if (oldSocket != null) {
            oldSocket.disconnect();
        }
    }

    public void remove(SocketIOClient socketIOClient) {
        if (socketIOClient.has("userId")) {
            users.remove(socketIOClient.get("userId"));
        }
    }

}
