package com.studymate.repository;

import com.studymate.entity.Announcement;
import com.studymate.entity.Favorite;
import com.studymate.entity.authentication.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query("""
        select exists(select fav from Favorite fav
                where fav.user.id = :userId and fav.announcement.id = :announcementId)
        """)
    Boolean isLiked(Long userId, Long announcementId);

    Boolean existsByUserIdAndAnnouncementId(Long userId, Long announcementId);

}
