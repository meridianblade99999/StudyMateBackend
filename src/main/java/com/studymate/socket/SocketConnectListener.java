package com.studymate.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.studymate.entity.authentication.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SocketConnectListener implements ConnectListener {

    @Autowired
    private SocketUserList socketUserList;

    @Autowired
    private SocketAuthorization socketAuthorization;

    public SocketConnectListener(@Autowired SocketIOServer server) {
        server.addConnectListener(this);
    }

    @Override
    public void onConnect(SocketIOClient socketIOClient) {
        User user = socketAuthorization.authorize(socketIOClient.getHandshakeData());
        if (user != null) {
            socketUserList.add(user.getId(), socketIOClient);
        } else {
            socketIOClient.disconnect();
        }
    }

}
