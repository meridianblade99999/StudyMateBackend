package com.studymate.services;

import com.studymate.dto.announcement.AnnouncementDto;
import com.studymate.dto.announcement.AnnouncementResponseDto;
import com.studymate.dto.announcement.AnnouncementUpdateDto;
import com.studymate.entity.Announcement;
import com.studymate.entity.Tag;
import com.studymate.entity.authentication.User;
import com.studymate.repository.AnnouncementRepository;
import com.studymate.repository.FavoriteRepository;
import com.studymate.repository.TagRepository;
import com.studymate.util.ColourUtil;
import com.studymate.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.naming.NoPermissionException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class AnnouncementService {

    private final TagRepository tagRepository;
    private final AnnouncementRepository announcementRepository;
    private final FavoriteRepository favoriteRepository;
    private final MapperUtil mapper;
    private final ColourUtil colourUtil;

    public Announcement create(User user, AnnouncementDto announcementDto) {
        Announcement announcementEntity = mapper.toAnnouncementEntity(announcementDto);
        announcementEntity.setBgColor(colourUtil.createRandomHslColor());
        announcementEntity.setUser(user);
        announcementEntity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return announcementRepository.save(announcementEntity);
    }

    public void addTag(Announcement announcement, String tag) {
        Tag tagEntity = tagRepository.getOrCreate(tag);
        tagEntity.getAnnouncements().add(announcement);
        announcement.getTags().add(tagEntity);
        tagRepository.save(tagEntity);
    }

    public List<AnnouncementResponseDto> getAnnouncements(int page, int pageSize, String tags, Boolean gender, Integer minAge, Integer maxAge) {
        List<Long> tagIds = null;
        if (tags != null) {
            String[] tagArray = tags.split(";");
            tagIds = new ArrayList<>(tagArray.length);
            for (String tag : tagArray) {
                Tag tagEntity = tagRepository.findByName(tag.toLowerCase());
                if (tagEntity != null) {
                    tagIds.add(tagEntity.getId());
                }
            }
        }
        List<Announcement> announcementList = announcementRepository.findFilterAnnouncements(PageRequest.of(page, pageSize), tags, tagIds, gender, minAge, maxAge);
        return mapper.getAnnouncementResponseDtos(announcementList, false);
    }

    public AnnouncementResponseDto getShortAnouncement(long announcementId) throws NoSuchElementException {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow();
        AnnouncementResponseDto responseDto = mapper.toAnnouncementResponseDto(announcement, false);
        mapper.addTagsToAnnouncementResponse(announcement, responseDto);
        return responseDto;
    }

    public AnnouncementResponseDto getFullAnnouncement(long announcementId, User user) throws NoSuchElementException {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow();
        AnnouncementResponseDto responseDto = mapper.toAnnouncementResponseDto(announcement, true);
        mapper.addTagsToAnnouncementResponse(announcement, responseDto);
        if (user != null) {
            responseDto.setLiked(favoriteRepository.isLiked(user.getId(), announcement.getId()));
        } else {
            responseDto.setLiked(false);
        }
        return responseDto;
    }

    public List<AnnouncementResponseDto> getUserAnnouncements(long userId, int page, int pageSize) {
        List<Announcement> announcementList = announcementRepository.findAllByUser_IdOrderByCreatedAtDesc(userId, PageRequest.of(page, pageSize));
        return mapper.getAnnouncementResponseDtos(announcementList, false);
    }

    public void updateAnnouncement(User user, long announcementId, AnnouncementUpdateDto updateDto) throws NoSuchElementException, NoPermissionException {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow();
        if (announcement.getUser().getId() != user.getId()) {
            throw new NoPermissionException("Wrong user");
        }
        announcement.setTitle(updateDto.getTitle());
        announcement.setDescription(updateDto.getDescription());
        for (Tag tag : announcement.getTags()) {
            tag.getAnnouncements().remove(announcement);
        }
        announcement.getTags().clear();
        for (String tagName : updateDto.getTags()) {
            announcement.getTags().add(tagRepository.getOrCreate(tagName));
        }
        announcementRepository.save(announcement);
    }

    public void deleteAnnouncement(User user, long announcementId) throws NoSuchElementException, NoPermissionException {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow();
        if (announcement.getUser().getId() != user.getId()) {
            throw new NoPermissionException("Wrong user");
        }
        announcementRepository.delete(announcement);
    }

}
