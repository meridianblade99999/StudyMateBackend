package com.studymate.repository;

import com.studymate.entity.Announcement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    Optional<Announcement> findById(Long id);
    List<Announcement> findAllByOrderByCreatedAtDesc(Pageable pageable);
    List<Announcement> findAllByUser_IdOrderByCreatedAtDesc(long userId, Pageable pageable);

    @Query("""
        select a from Announcement a where exists(select fav from Favorite fav where fav.announcement.id = a.id and fav.user.id = :userId)
        """)
    List<Announcement> findLikeAnnouncements(long userId, Pageable pageable);

}
