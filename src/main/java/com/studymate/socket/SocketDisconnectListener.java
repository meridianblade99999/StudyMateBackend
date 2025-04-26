package com.studymate.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SocketDisconnectListener implements DisconnectListener {

    @Autowired
    private SocketUserList socketUserList;

    public SocketDisconnectListener(@Autowired SocketIOServer server) {
        server.addDisconnectListener(this);
    }

    @Override
    public void onDisconnect(SocketIOClient socketIOClient) {
        socketUserList.remove(socketIOClient);
    }

}
