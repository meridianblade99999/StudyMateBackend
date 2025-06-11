package com.studymate.services;

import com.studymate.dto.announcement.AnnouncementResponseDto;
import com.studymate.entity.Tag;
import com.studymate.entity.authentication.User;
import com.studymate.repository.AnnouncementRepository;
import com.studymate.repository.TagRepository;
import com.studymate.repository.authentication.UserRepository;
import com.studymate.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class SearchService {

    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final AnnouncementRepository announcementRepository;
    private final MapperUtil mapper;

    /**
     * Метод выполняет поиск объявлений по указанным параметрам.
     *
     * @param title заголовок объявления, используется для поиска объявлений с совпадающим заголовком; если null, поиск по заголовку не выполняется
     * @param username имя пользователя, используется для поиска объявлений, созданных указанным пользователем; если null, поиск по пользователю не выполняется
     * @param tag тег, используется для поиска объявлений, связанных с указанным тегом; если null, поиск по тегу не выполняется
     * @return список объектов AnnouncementResponseDto, соответствующих критериям поиска; если ничего не найдено, возвращается пустой список
     */
    public List<AnnouncementResponseDto> searchAnnouncements(int page, int pageSize, String title, String username, String tag) {
        if (title != null) {
            return mapper.getAnnouncementResponseDtos(announcementRepository.findByTitle(PageRequest.of(page, pageSize), title), false);
        } else if (username != null) {
            return mapper.getAnnouncementResponseDtos(announcementRepository.findByUsername(PageRequest.of(page, pageSize), username), false);
        } else if (tag != null) {
            Tag tagEntity = tagRepository.findByName(tag.toLowerCase());
            if (tagEntity != null) {
                return mapper.getAnnouncementResponseDtos(announcementRepository.findByTag(PageRequest.of(page, pageSize), tagEntity.getId()), false);
            }
        }
        return Collections.emptyList();
    }

    public List<String> searchTitles(String title) {
        return announcementRepository.findTitles(PageRequest.of(0, 5), title.toLowerCase());
    }

    public List<String> searchTags(String tag) {
        return tagRepository.findTags(PageRequest.of(0, 5), tag.toLowerCase());
    }

    public List<String> searchUsernames(String username) {
        return userRepository.findUsers(PageRequest.of(0, 5), username.toLowerCase());
    }

    public List<AnnouncementResponseDto> searchAnnouncementsByTitle(int page, int pageSize, String title) {
        return mapper.getAnnouncementResponseDtos(announcementRepository.findByTitle(PageRequest.of(page, pageSize), title), false);
    }

    public List<AnnouncementResponseDto> searchAnnouncementsByTag(int page, int pageSize, String tag) {
        Tag tagEntity = tagRepository.findByName(tag.toLowerCase());
        if (tagEntity != null) {
            return mapper.getAnnouncementResponseDtos(announcementRepository.findByTag(PageRequest.of(page, pageSize), tagEntity.getId()), false);
        }
        return Collections.emptyList();
    }

    public long searchUserId(String username) throws NoSuchElementException {
        User user = userRepository.findByUsername(username).orElseThrow();
        return user.getId();
    }

}
