package com.studymate.util;

import com.studymate.dto.announcement.AnnouncementDto;
import com.studymate.dto.announcement.AnnouncementResponseDto;
import com.studymate.dto.response.ResponseDto;
import com.studymate.dto.tag.TagCreateDto;
import com.studymate.dto.tag.TagResponseDto;
import com.studymate.dto.user.UserResponseDto;
import com.studymate.entity.Announcement;
import com.studymate.entity.Response;
import com.studymate.entity.Tag;
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
        announcementResponseDto.setUserName(announcement.getUser().getUsername());
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
        responseDto.setUsername(response.getUser().getUsername());
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

}
