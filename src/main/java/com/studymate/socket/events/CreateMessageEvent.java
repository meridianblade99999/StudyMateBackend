package com.studymate.socket.events;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.studymate.dto.chat.ChatMessageResponseDto;
import com.studymate.entity.Chat;
import com.studymate.entity.ChatUser;
import com.studymate.entity.Message;
import com.studymate.repository.ChatRepository;
import com.studymate.repository.ChatUserRepository;
import com.studymate.repository.MessageRepository;
import com.studymate.socket.SocketEventListener;
import com.studymate.socket.SocketIOConfig;
import com.studymate.socket.SocketUserList;
import com.studymate.socket.model.CreateMessageRequest;
import com.studymate.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class CreateMessageEvent extends SocketEventListener<CreateMessageRequest> {

    @Autowired
    private ChatUserRepository chatUserRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MapperUtil mapperUtil;

    @Autowired
    private SocketUserList socketUserList;

    public CreateMessageEvent(SocketIOServer server) {
        super(server);
    }

    @Override
    protected String getName() {
        return "new_message";
    }

    @Override
    public void onData(
        SocketIOClient socketIOClient,
        CreateMessageRequest createMessageRequest,
        AckRequest ackRequest
    ) throws Exception {
        if (!validate(createMessageRequest)) {
            return;
        }
        ChatUser chatUser = chatUserRepository.findByChatIdAndUserId(
            createMessageRequest.getChatId(), socketIOClient.get("userId")).orElse(null);
        if (chatUser == null) {
            return;
        }

        Message message = new Message();
        message.setUser(chatUser.getUser());
        message.setChat(chatUser.getChat());
        message.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        message.setContent(createMessageRequest.getContent());
        if (createMessageRequest.getAnsweredMessageId() != null) {
            message.setAnsweredMessage(messageRepository.findByIdAndChatId(createMessageRequest.getAnsweredMessageId(),
                chatUser.getChat().getId()));
        }
        messageRepository.save(message);

        ChatMessageResponseDto chatMessageResponseDto = mapperUtil.toChatMessageResponseDto(message);
        socketUserList.broadcastToChat(chatUser.getChat().getId(), "new_message", chatMessageResponseDto);
    }

}
