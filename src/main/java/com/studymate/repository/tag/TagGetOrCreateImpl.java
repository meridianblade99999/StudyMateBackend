package com.studymate.repository.tag;

import com.studymate.entity.Tag;
import com.studymate.util.ColourUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TagGetOrCreateImpl implements TagGetOrCreate {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ColourUtil colourUtil;

    @Transactional
    @Override
    public Tag getOrCreate(String tag) {
        tag = tag.toLowerCase();
        Query query = em.createQuery("""
            select t from Tag t where t.name = :name
            """, Tag.class)
                .setParameter("name", tag);

        if (!query.getResultList().isEmpty()) {
            return (Tag) query.getSingleResult();
        }

        em.createNativeQuery("""
            insert into tag (name, color) values (:name, :color)
            """)
                .setParameter("name", tag)
                .setParameter("color", colourUtil.createRandomHslColor()).executeUpdate();

        return em.createQuery("""
            select t from Tag t where t.name = :name
            """, Tag.class).setParameter("name", tag).getSingleResult();
    }

}
