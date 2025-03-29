package com.studymate.services;

import com.studymate.dto.announcement.AnnouncementResponseDto;
import com.studymate.dto.favorite.FavoriteCreateRequestDto;
import com.studymate.entity.Announcement;
import com.studymate.entity.Favorite;
import com.studymate.entity.authentication.User;
import com.studymate.mapper.MapperUtil;
import com.studymate.repository.AnnouncementRepository;
import com.studymate.repository.FavoriteRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.naming.NoPermissionException;
import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final AnnouncementRepository announcementRepository;
    private final MapperUtil mapper;

    public void createFavorite(User user, FavoriteCreateRequestDto favoriteCreateRequestDto) throws NoSuchElementException {
        if (favoriteRepository.existsByUserIdAndAnnouncementId(user.getId(), favoriteCreateRequestDto.getAnnouncementId())) {
            return;
        }
        Announcement announcement = announcementRepository.findById(favoriteCreateRequestDto.getAnnouncementId()).orElseThrow();
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setAnnouncement(announcement);
        favorite.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        favoriteRepository.save(favorite);
    }

    public List<AnnouncementResponseDto> getUserFavorites(long userId, int page, int pageSize) {
        List<Announcement> announcementList = announcementRepository.findLikeAnnouncements(userId, PageRequest.of(page, pageSize));
        return mapper.getAnnouncementResponseDtos(announcementList);
    }

    public void deleteFavorite(User user, long favoriteId) throws NoSuchElementException, NoPermissionException {
        Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow();
        if (favorite.getUser().getId() != user.getId()) {
            throw new NoPermissionException("Wrong user");
        }
        favoriteRepository.delete(favorite);
    }


}
