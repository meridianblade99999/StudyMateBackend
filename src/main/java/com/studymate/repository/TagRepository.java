package com.studymate.repository;

import com.studymate.entity.Tag;
import com.studymate.repository.tag.TagGetOrCreate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long>, TagGetOrCreate {

    Tag findByName(String name);

    @Query("""
        select t.name from Tag t
        where lower(t.name) like %:tag%
        """)
    List<String> findTags(Pageable pageable, String tag);
}
