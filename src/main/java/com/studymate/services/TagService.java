package com.studymate.services;

import com.studymate.dto.tag.TagCreateDto;
import com.studymate.dto.tag.TagResponseDto;
import com.studymate.entity.Response;
import com.studymate.entity.Tag;
import com.studymate.entity.authentication.User;
import com.studymate.mapper.MapperUtil;
import com.studymate.repository.TagRepository;
import com.studymate.repository.authentication.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.naming.NoPermissionException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;

    public void createTag(TagCreateDto tagCreateDto) {
        tagRepository.getOrCreate(tagCreateDto.getName(), tagCreateDto.getColor());
    }

    public List<TagResponseDto> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        List<TagResponseDto> tagResponseDtos = new ArrayList<>();
        for (Tag tag : tags) {
            mapperUtil.toTagResponseDto(tag);
        }
        return tagResponseDtos;
    }

    public TagResponseDto getTag(long id) throws NoSuchElementException {
        Tag tag = tagRepository.findById(id).orElseThrow();
        return mapperUtil.toTagResponseDto(tag);
    }

    public void deleteTag(User user, long tagId) throws NoSuchElementException {
        Tag tag = tagRepository.findById(tagId).orElseThrow();
        tagRepository.delete(tag);
    }

}
