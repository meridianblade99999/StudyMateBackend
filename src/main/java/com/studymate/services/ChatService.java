package com.studymate.services;

import com.studymate.dto.chat.*;
import com.studymate.entity.Chat;
import com.studymate.entity.ChatSettings;
import com.studymate.entity.ChatUser;
import com.studymate.entity.Message;
import com.studymate.entity.authentication.User;
import com.studymate.repository.ChatRepository;
import com.studymate.repository.ChatSettingsRepository;
import com.studymate.repository.ChatUserRepository;
import com.studymate.repository.MessageRepository;
import com.studymate.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.naming.NoPermissionException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatService {

    private final MapperUtil mapperUtil;
    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;
    private final MessageRepository messageRepository;
    private final ChatSettingsRepository chatSettingsRepository;

    public List<ChatResponseDto> getChatList(User user, int page, int pageSize) {
        List<ChatResponseDto> chats = new ArrayList<>();
        for (ChatUser chatUser : chatUserRepository.findByUserId(user.getId(), PageRequest.of(page, pageSize))) {
            Chat chat = chatUser.getChat();
            ChatResponseDto chatResponseDto = mapperUtil.toChatResponseDto(chat);
            chats.add(chatResponseDto);

            chatResponseDto.setChatSettings(mapperUtil.toChatSettingsResponseDto(chatUser.getChatSettings()));

            ChatResponseUserDto[] users = new ChatResponseUserDto[chat.getChatUsers().size()];
            for (int i = 0; i < chat.getChatUsers().size(); i++) {
                users[i] = mapperUtil.toChatResponseUserDto(chat.getChatUsers().get(i));
            }
            chatResponseDto.setUsers(users);

            Optional<Message> messageOptional = messageRepository.findFirstByChatIdOrderByIdDesc(chat.getId());
            if (messageOptional.isPresent()) {
                chatResponseDto.setLastMessage(mapperUtil.toChatLastMessageDto(messageOptional.get()));
            }
        }
        return chats;
    }

    public ChatResponseDto getChat(User user, long chatId, int page, int pageSize) throws NoSuchElementException {
        ChatUser chatUser = chatUserRepository.findByChatIdAndUserId(chatId, user.getId()).orElseThrow();
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        ChatResponseDto chatResponseDto = mapperUtil.toChatResponseDto(chat);

        chatResponseDto.setChatSettings(mapperUtil.toChatSettingsResponseDto(chatUser.getChatSettings()));

        ChatResponseUserDto[] users = new ChatResponseUserDto[chat.getChatUsers().size()];
        for (int i = 0; i < chat.getChatUsers().size(); i++) {
            users[i] = mapperUtil.toChatResponseUserDto(chat.getChatUsers().get(i));
        }
        chatResponseDto.setUsers(users);

        List<Message> messages = messageRepository.findByChatIdOrderByIdDesc(chatId, PageRequest.of(page, pageSize));
        ChatMessageResponseDto[] messagesArray = new ChatMessageResponseDto[messages.size()];
        for (int i = 0; i < messages.size(); i++) {
            messagesArray[i] = mapperUtil.toChatMessageResponseDto(messages.get(i));
        }
        chatResponseDto.setMessages(messagesArray);

        return chatResponseDto;
    }

    public ChatSettingsResponseDto updateChatSettings(User user, long chatId, ChatSettingsRequestDto chatSettingsRequestDto) throws NoSuchElementException {
        ChatUser chatUser = chatUserRepository.findByChatIdAndUserId(chatId, user.getId()).orElseThrow();
        ChatSettings chatSettings = chatUser.getChatSettings();
        if (chatSettings == null) {
            chatSettings = new ChatSettings();
            chatUser.setChatSettings(chatSettings);
            chatSettings.setChatUser(chatUser);
        }
        chatSettings.setMuted(chatSettingsRequestDto.getMuted());
        chatSettings.setNotifications(chatSettingsRequestDto.getNotifications());
        chatSettingsRepository.save(chatSettings);
        return mapperUtil.toChatSettingsResponseDto(chatSettings);
    }

    public void deleteChat(User user, long chatId) throws NoSuchElementException {
        ChatUser chatUser = chatUserRepository.findByChatIdAndUserId(chatId, user.getId()).orElseThrow();
        chatUserRepository.delete(chatUser);
    }
}
