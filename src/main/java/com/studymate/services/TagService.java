package com.studymate.services;

import com.studymate.entity.Tag;
import com.studymate.repository.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepository;

    public Tag getTag(String tag) {
        synchronized (this) {
            Tag tagEntity = tagRepository.findByName(tag);
            if (tagEntity == null) {
                tagEntity = new Tag();
                tagEntity.setName(tag);
                tagEntity = tagRepository.save(tagEntity);
            }
            return tagEntity;
        }
    }

}
