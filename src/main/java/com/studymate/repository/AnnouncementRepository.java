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
        select a.* from Announcement a 
        join User u on u.id = a.user_id where 
        (u.gender = :gender or :gender is null) and
        (:minAge is null or TIMESTAMPDIFF(YEAR, u.birthday, CURDATE()) >= :minAge) and
        (:maxAge is null or TIMESTAMPDIFF(YEAR, u.birthday, CURDATE()) <= :maxAge) and
        (:findByTags = 0 or exists(select tag_id from announcement_tag ant where ant.announcement_id=a.id and ant.tag_id in (:tagIds)))
        order by a.created_at desc
        """, nativeQuery = true)
    List<Announcement> findFilterAnnouncements(Pageable pageable, boolean findByTags, List<Long> tagIds, Boolean gender, Integer minAge, Integer maxAge);

    @Query("""
        select a from Announcement a
        where a.title like %:title% 
        order by a.id desc
        """)
    List<Announcement> findByTitle(Pageable pageable,String title);

    @Query("""
        select a from Announcement a
        where exists(select 1 from User u where u=a.user and u.username like %:username%)
        order by a.id desc
        """)
    List<Announcement> findByUsername(Pageable pageable,String username);

    @Query(value = """
        select a.* from Announcement a
        where exists(select 1 from announcement_tag at where at.announcement_id=a.id and at.tag_id=:tagId)
        """, nativeQuery = true)
    List<Announcement> findByTag(Pageable pageable, long tagId);
}
