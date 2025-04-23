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

/**
 * Сервис для управления чатами, их участниками, настройками и сообщениями.
 *
 * Это основной класс для выполнения операций над чатами, такими как получение списка чатов,
 * получение информации об одном чате, обновление настроек чата и удаление пользователя из чата.
 *
 * Конструктор класса автоматически генерируется с помощью {@code @AllArgsConstructor}.
 */
@Service
@AllArgsConstructor
public class ChatService {

    private final MapperUtil mapperUtil;
    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;
    private final MessageRepository messageRepository;
    private final ChatSettingsRepository chatSettingsRepository;

    /**
     * Возвращает список чатов для указанного пользователя с учетом параметров разбиения на страницы.
     *
     * @param user объект пользователя, для которого необходимо получить список чатов
     * @param page номер страницы для пагинации (начиная с 0)
     * @param pageSize количество элементов на одной странице
     * @return список объектов ChatResponseDto, содержащих информацию о чатах пользователя
     */
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

    /**
     * Метод получает информацию о чате, включая настройки, участников и сообщения.
     *
     * @param user      пользователь, для которого осуществляется запрос.
     * @param chatId    идентификатор чата.
     * @param page      номер страницы для пагинации сообщений.
     * @param pageSize  количество сообщений на странице.
     * @return объект ChatResponseDto, содержащий информацию о чате, его настройках, участниках и сообщениях.
     * @throws NoSuchElementException если информация о чате или о пользователе в чате не найдена.
     */
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

    /**
     * Обновляет настройки чата для пользователя.
     *
     * @param user объект пользователя, для которого необходимо обновить настройки чата
     * @param chatId идентификатор чата, в котором необходимо обновить настройки
     * @param chatSettingsRequestDto объект запроса, содержащий новые настройки чата
     * @return объект ChatSettingsResponseDto с обновленными настройками чата
     * @throws NoSuchElementException если пользователь не найден в указанном чате
     */
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

    /**
     * Удаляет чат для заданного пользователя.
     *
     * @param user пользователь, для которого необходимо удалить чат
     * @param chatId идентификатор чата, который требуется удалить
     * @throws NoSuchElementException если чат не найден для указанного пользователя
     */
    public void deleteChat(User user, long chatId) throws NoSuchElementException {
        ChatUser chatUser = chatUserRepository.findByChatIdAndUserId(chatId, user.getId()).orElseThrow();
        chatUserRepository.delete(chatUser);
    }
}
