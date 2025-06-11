package com.studymate.socket.events;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.studymate.dto.response.ResponseDto;
import com.studymate.socket.SocketEventListener;
import com.studymate.socket.SocketUserList;
import com.studymate.socket.model.SocketMessage;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestMessageEventListener extends SocketEventListener<SocketMessage> {

    @Autowired
    private SocketUserList socketUserList;

    public TestMessageEventListener(SocketIOServer server) {
        super(server);
    }

    @Override
    protected String getName() {
        return "chat";
    }

    @Override
    public void onData(SocketIOClient socketIOClient, SocketMessage message, AckRequest ackRequest) throws Exception {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        if (!validator.validate(message).isEmpty())
            return;
        ResponseDto dto = new ResponseDto();
        dto.setId((long) 1);
        dto.setAnnouncementId((long) 2);
        socketIOClient.sendEvent("chat", dto);
        socketUserList.broadcastToChat(1, "new_message", 1);
    }

}
