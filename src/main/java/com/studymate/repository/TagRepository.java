package com.studymate.repository;

import com.studymate.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findByName(String name);

    @Query(value = """
            WITH new_tag AS (
              INSERT INTO tags (name, color)
              VALUES (:name, :color)
              ON CONFLICT (name) DO NOTHING
              RETURNING *
            )
            SELECT * FROM new_tag
            UNION\s
            SELECT * FROM tags
            WHERE name = :name
          """, nativeQuery = true)
    Tag getOrCreate(String name, String color);
}
