package com.studymate.repository.tag;

import com.studymate.entity.Tag;
import com.studymate.util.ColourUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TagGetOrCreateImpl implements TagGetOrCreate {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ColourUtil colourUtil;

    @Override
    public Tag getOrCreate(String tag) {
        tag = tag.toLowerCase();
        String color = colourUtil.createRandomHslColor();
        Query query = em.createNativeQuery("""
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
            """, Tag.class);
        query.setParameter("name", tag);
        query.setParameter("color", color);
        return (Tag) query.getSingleResult();
    }

}
