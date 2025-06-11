package com.studymate.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.studymate.entity.Chat;
import com.studymate.repository.ChatUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@Component
public class SocketUserList {

    private final Map<Long, SocketIOClient> users = new ConcurrentHashMap<>();

    private ChatUserRepository chatUserRepository;

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

    public void broadcastToChat(long chatId, String eventName, Object data) {
        for (long userId : chatUserRepository.getUserIdsByChatId(chatId)) {
            SocketIOClient socketIOClient = users.get(userId);
            if (socketIOClient != null) {
                socketIOClient.sendEvent(eventName, data);
            }
        }
    }

}
