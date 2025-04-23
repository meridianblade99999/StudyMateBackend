package com.studymate.services;

import com.studymate.dto.tag.TagCreateDto;
import com.studymate.dto.tag.TagResponseDto;
import com.studymate.entity.Tag;
import com.studymate.entity.authentication.User;
import com.studymate.repository.TagRepository;
import com.studymate.repository.authentication.UserRepository;
import com.studymate.util.ColourUtil;
import com.studymate.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Сервис для работы с тегами. Содержит методы для создания, получения всех тегов,
 * получения конкретного тега по идентификатору и удаления тега.
 */
@AllArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;

    /**
     * Создает или получает существующий тег на основе переданных данных.
     *
     * @param tagCreateDto объект, содержащий информацию о создаваемом теге, включая имя тега
     */
    public void createTag(TagCreateDto tagCreateDto) {
        tagRepository.getOrCreate(tagCreateDto.getName());
    }

    /**
     * Возвращает список всех тегов в формате DTO.
     *
     * @return список объектов типа TagResponseDto, представляющих все теги
     */
    public List<TagResponseDto> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        List<TagResponseDto> tagResponseDtos = new ArrayList<>();
        for (Tag tag : tags) {
            mapperUtil.toTagResponseDto(tag);
        }
        return tagResponseDtos;
    }

    /**
     * Возвращает объект TagResponseDto, представляющий тег с указанным идентификатором.
     * Если тег с заданным идентификатором не найден, выбрасывается исключение NoSuchElementException.
     *
     * @param id уникальный идентификатор тега
     * @return объект TagResponseDto, содержащий информацию о теге
     * @throws NoSuchElementException если тег с указанным идентификатором не найден
     */
    public TagResponseDto getTag(long id) throws NoSuchElementException {
        Tag tag = tagRepository.findById(id).orElseThrow();
        return mapperUtil.toTagResponseDto(tag);
    }

    /**
     * Удаляет тег с указанным идентификатором.
     * Если тег с заданным идентификатором не найден, выбрасывается исключение NoSuchElementException.
     *
     * @param user пользователь, совершающий удаление тега
     * @param tagId уникальный идентификатор тега, который требуется удалить
     * @throws NoSuchElementException если тег с указанным идентификатором не найден
     */
    public void deleteTag(User user, long tagId) throws NoSuchElementException {
        Tag tag = tagRepository.findById(tagId).orElseThrow();
        tagRepository.delete(tag);
    }

}
