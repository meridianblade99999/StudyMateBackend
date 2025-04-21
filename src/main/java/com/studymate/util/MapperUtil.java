package com.studymate.util;

import com.studymate.dto.announcement.AnnouncementDto;
import com.studymate.dto.announcement.AnnouncementResponseDto;
import com.studymate.dto.chat.*;
import com.studymate.dto.response.ResponseDto;
import com.studymate.dto.tag.TagCreateDto;
import com.studymate.dto.tag.TagResponseDto;
import com.studymate.dto.user.UserResponseDto;
import com.studymate.entity.*;
import com.studymate.entity.authentication.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class MapperUtil {

    public Announcement toAnnouncementEntity(AnnouncementDto announcementDto) {
        Announcement announcementEntity = new Announcement();
        announcementEntity.setTitle(announcementDto.getTitle());
        announcementEntity.setDescription(announcementDto.getDescription());
        return announcementEntity;
    }

    public AnnouncementResponseDto toAnnouncementResponseDto(Announcement announcement, boolean withDescription) {
        AnnouncementResponseDto announcementResponseDto = new AnnouncementResponseDto();
        announcementResponseDto.setId(announcement.getId());
        announcementResponseDto.setTitle(announcement.getTitle());
        if (withDescription) {
            announcementResponseDto.setDescription(announcement.getDescription());
        }
        announcementResponseDto.setBgColor(announcement.getBgColor());
        announcementResponseDto.setUserId(announcement.getUser().getId());
        announcementResponseDto.setName(announcement.getUser().getName());
        return announcementResponseDto;
    }

    public List<AnnouncementResponseDto> getAnnouncementResponseDtos(
            List<Announcement> announcementList, boolean withDescription) {
        List<AnnouncementResponseDto> announcementResponseDtoList = new ArrayList<>(announcementList.size());
        for (Announcement announcement : announcementList) {
            AnnouncementResponseDto responseDto = toAnnouncementResponseDto(announcement, withDescription);
            announcementResponseDtoList.add(responseDto);
            addTagsToAnnouncementResponse(announcement, responseDto);
        }
        return announcementResponseDtoList;
    }

    public void addTagsToAnnouncementResponse(Announcement announcement,
                                              AnnouncementResponseDto announcementResponseDto) {
        TagResponseDto[] tags = new TagResponseDto[announcement.getTags().size()];
        for (int i = 0; i < tags.length; i++) {
            tags[i] = toTagResponseDto(announcement.getTags().get(i));
        }
        announcementResponseDto.setTags(tags);
    }

    public TagResponseDto toTagResponseDto(Tag tag) {
        TagResponseDto tagResponseDto = new TagResponseDto();
        tagResponseDto.setId(tag.getId());
        tagResponseDto.setName(tag.getName());
        tagResponseDto.setColor(tag.getColor());
        return tagResponseDto;
    }

    public ResponseDto toResponseDto(Response response) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setId(response.getId());
        responseDto.setDescription(response.getDescription());
        responseDto.setUserId(response.getUser().getId());
        responseDto.setName(response.getUser().getName());
        responseDto.setAnnouncementId(response.getAnnouncement().getId());
        return responseDto;
    }

    public UserResponseDto toUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setName(user.getName());
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setDescription(user.getDescription());
        userResponseDto.setLocation(user.getLocation());
        userResponseDto.setGender(user.getGender());
        userResponseDto.setBirthday(user.getBirthday());
        userResponseDto.setCreatedAt(user.getCreatedAt());
        return userResponseDto;
    }

    public ChatResponseDto toChatResponseDto(Chat chat) {
        ChatResponseDto chatResponseDto = new ChatResponseDto();
        chatResponseDto.setId(chat.getId());
        chatResponseDto.setName(chat.getTitle());
        return chatResponseDto;
    }

    public ChatSettingsResponseDto toChatSettingsResponseDto(ChatSettings chatSettings) {
        ChatSettingsResponseDto chatSettingsResponseDto = new ChatSettingsResponseDto();
        chatSettingsResponseDto.setId(chatSettings.getId());
        chatSettingsResponseDto.setMuted(chatSettings.getMuted());
        chatSettingsResponseDto.setNotifications(chatSettings.getNotifications());
        return chatSettingsResponseDto;
    }

    public ChatResponseUserDto toChatResponseUserDto(ChatUser chatUser) {
        ChatResponseUserDto chatResponseUserDto = new ChatResponseUserDto();
        chatResponseUserDto.setId(chatUser.getId());
        chatResponseUserDto.setName(chatUser.getUser().getName());
        chatResponseUserDto.setUsername(chatUser.getUser().getUsername());
        return chatResponseUserDto;
    }

    public ChatLastMessageDto toChatLastMessageDto(Message message) {
        ChatLastMessageDto chatLastMessageDto = new ChatLastMessageDto();
        chatLastMessageDto.setId(message.getId());
        chatLastMessageDto.setUserId(message.getUser().getId());
        chatLastMessageDto.setUsername(message.getUser().getUsername());
        chatLastMessageDto.setCreatedAt(message.getCreatedAt());
        chatLastMessageDto.setContent(message.getContent());
        return chatLastMessageDto;
    }

    public ChatMessageResponseDto toChatMessageResponseDto(Message message) {
        ChatMessageResponseDto chatMessageResponseDto = new ChatMessageResponseDto();
        chatMessageResponseDto.setId(message.getId());
        chatMessageResponseDto.setUserId(message.getUser().getId());
        chatMessageResponseDto.setUserName(message.getUser().getUsername());
        chatMessageResponseDto.setChatId(message.getChat().getId());
        chatMessageResponseDto.setAnsweredMessageId(message.getAnsweredMessage() != null ? message.getAnsweredMessage().getId() : null);
        chatMessageResponseDto.setCreatedAt(message.getCreatedAt());
        chatMessageResponseDto.setUpdatedAt(message.getUpdatedAt());
        chatMessageResponseDto.setContent(message.getContent());
        return chatMessageResponseDto;
    }
}
