package com.studymate.repository;

import com.studymate.entity.Tag;
import com.studymate.repository.tag.TagGetOrCreate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TagRepository extends JpaRepository<Tag, Long>, TagGetOrCreate {

    Tag findByName(String name);

}
