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

    @Query(value = """
        select a.* from Announcements a 
        join Users u on u.id = a.user_id where 
        (u.gender = :gender or :gender is null) and
        (DATE_PART('Year', age(u.birthday)) >= :minAge or :minAge is null) and
        (DATE_PART('Year', age(u.birthday)) <= :maxAge or :maxAge is null) and
        (:tags is null or exists(select tag_id from announcement_tag ant where ant.announcement_id=a.id and ant.tag_id in (:tagIds)))
        order by a.created_at desc
        """, nativeQuery = true)
    List<Announcement> findFilterAnnouncements(Pageable pageable, String tags, List<Long> tagIds, Boolean gender, Integer minAge, Integer maxAge);

}
