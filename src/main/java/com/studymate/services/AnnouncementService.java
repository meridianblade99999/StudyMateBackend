package com.studymate.services;

import com.studymate.dto.announcement.AnnouncementCreateRequestDto;
import com.studymate.dto.announcement.AnnouncementResponseDto;
import com.studymate.dto.announcement.AnnouncementUpdateDto;
import com.studymate.entity.Announcement;
import com.studymate.entity.Tag;
import com.studymate.entity.authentication.User;
import com.studymate.repository.AnnouncementRepository;
import com.studymate.repository.FavoriteRepository;
import com.studymate.repository.TagRepository;
import com.studymate.util.ColourUtil;
import com.studymate.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.naming.NoPermissionException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Сервис для работы с объявлениями. Предоставляет функционал для создания,
 * обновления, удаления и получения объявлений, а также для работы с тегами
 * и пользовательскими действиями, такими как лайки.
 */
@AllArgsConstructor
@Service
public class AnnouncementService {

    private final TagRepository tagRepository;
    private final AnnouncementRepository announcementRepository;
    private final FavoriteRepository favoriteRepository;
    private final MapperUtil mapper;
    private final ColourUtil colourUtil;

    /**
     * Создает новое объявление на основе данных пользователя и DTO.
     *
     * @param user пользователь, который создает объявление
     * @param announcementDto объект DTO, содержащий данные объявления
     * @return созданное объявление
     */
    public Announcement create(User user, AnnouncementCreateRequestDto announcementDto) {
        Announcement announcementEntity = mapper.toAnnouncementEntity(announcementDto);
        announcementEntity.setBgColor(colourUtil.createRandomHslColor());
        announcementEntity.setUser(user);
        announcementEntity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return announcementRepository.save(announcementEntity);
    }

    /**
     * Добавляет указанный тег к объявлению. Если тег еще не существует, он создается.
     *
     * @param announcement Объявление, к которому необходимо добавить тег.
     * @param tag Строковое представление тега, который необходимо добавить.
     */
    public void addTag(Announcement announcement, String tag) {
        Tag tagEntity = tagRepository.getOrCreate(tag);
        tagEntity.getAnnouncements().add(announcement);
        announcement.getTags().add(tagEntity);
        tagRepository.save(tagEntity);
    }

    /**
     * Метод для получения списка объявлений с возможностью фильтрации по тегам, полу и возрасту.
     *
     * @param page номер страницы для пагинации.
     * @param pageSize количество элементов на странице.
     * @param tags строка, содержащая теги, разделенные точкой с запятой, для фильтрации объявлений.
     * @param gender фильтр по полу; true для одного пола, false для другого, null для игнорирования фильтра.
     * @param minAge минимальный возраст для фильтрации объявлений.
     * @param maxAge максимальный возраст для фильтрации объявлений.
     * @return список объектов AnnouncementResponseDto, содержащий отфильтрованные объявления.
     */
    public List<AnnouncementResponseDto> getAnnouncements(int page, int pageSize, String tags, Boolean gender, Integer minAge, Integer maxAge) {
        List<Long> tagIds = null;
        if (tags != null) {
            String[] tagArray = tags.split(";");
            tagIds = new ArrayList<>(tagArray.length);
            for (String tag : tagArray) {
                Tag tagEntity = tagRepository.findByName(tag.toLowerCase());
                if (tagEntity != null) {
                    tagIds.add(tagEntity.getId());
                }
            }
        }
        List<Announcement> announcementList = announcementRepository.findFilterAnnouncements(PageRequest.of(page, pageSize), tagIds != null, tagIds, gender, minAge, maxAge);
        return mapper.getAnnouncementResponseDtos(announcementList, false);
    }

    /**
     * Возвращает краткое объявление по его идентификатору.
     *
     * @param announcementId идентификатор искомого объявления
     * @return объект типа AnnouncementResponseDto, содержащий краткую информацию об объявлении
     * @throws NoSuchElementException если объявление с указанным идентификатором не найдено
     */
    public AnnouncementResponseDto getShortAnouncement(long announcementId) throws NoSuchElementException {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow();
        AnnouncementResponseDto responseDto = mapper.toAnnouncementResponseDto(announcement, false);
        mapper.addTagsToAnnouncementResponse(announcement, responseDto);
        return responseDto;
    }

    /**
     * Возвращает полное объявление с учетом информации о текущем пользователе.
     *
     * @param announcementId идентификатор объявления, которое нужно получить
     * @param user пользователь, для которого необходимо определить дополнительную информацию (может быть null)
     * @return объект типа AnnouncementResponseDto, содержащий полную информацию об объявлении,
     *         включая информацию о том, было ли оно добавлено в избранное текущим пользователем
     * @throws NoSuchElementException если объявление с предоставленным идентификатором не найдено
     */
    public AnnouncementResponseDto getFullAnnouncement(long announcementId, User user) throws NoSuchElementException {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow();
        AnnouncementResponseDto responseDto = mapper.toAnnouncementResponseDto(announcement, true);
        mapper.addTagsToAnnouncementResponse(announcement, responseDto);
        if (user != null) {
            responseDto.setLiked(favoriteRepository.isLiked(user.getId(), announcement.getId()));
        } else {
            responseDto.setLiked(false);
        }
        return responseDto;
    }

    /**
     * Получает список объявлений пользователя с поддержкой пагинации.
     *
     * @param userId уникальный идентификатор пользователя, чьи объявления необходимо получить
     * @param page номер страницы для пагинации, начиная с 0
     * @param pageSize количество объявлений на одной странице
     * @return список объектов типа AnnouncementResponseDto, содержащих информацию об объявлениях пользователя
     */
    public List<AnnouncementResponseDto> getUserAnnouncements(long userId, int page, int pageSize) {
        List<Announcement> announcementList = announcementRepository.findAllByUser_IdOrderByCreatedAtDesc(userId, PageRequest.of(page, pageSize));
        return mapper.getAnnouncementResponseDtos(announcementList, false);
    }

    /**
     * Обновляет данные объявления.
     *
     * @param user Пользователь, выполняющий обновление. Должен соответствовать владельцу объявления.
     * @param announcementId Идентификатор объявления, которое необходимо обновить.
     * @param updateDto Объект, содержащий обновленные данные для объявления (заголовок, описание, теги).
     * @throws NoSuchElementException Если объявление с указанным идентификатором не найдено.
     * @throws NoPermissionException Если пользователь не является владельцем объявления.
     */
    public void updateAnnouncement(User user, long announcementId, AnnouncementUpdateDto updateDto) throws NoSuchElementException, NoPermissionException {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow();
        if (announcement.getUser().getId() != user.getId()) {
            throw new NoPermissionException("Wrong user");
        }
        announcement.setTitle(updateDto.getTitle());
        announcement.setDescription(updateDto.getDescription());
        for (Tag tag : announcement.getTags()) {
            tag.getAnnouncements().remove(announcement);
        }
        announcement.getTags().clear();
        for (String tagName : updateDto.getTags()) {
            Tag tagEntity = tagRepository.getOrCreate(tagName);
            announcement.getTags().add(tagEntity);
            tagEntity.getAnnouncements().add(announcement);
            tagRepository.save(tagEntity);
        }
        announcementRepository.save(announcement);
    }

    /**
     * Удаляет объявление по его идентификатору, если у пользователя есть разрешение на выполнение этой операции.
     *
     * @param user объект пользователя, выполняющего удаление
     * @param announcementId идентификатор объявления, которое нужно удалить
     * @throws NoSuchElementException если объявление с указанным идентификатором не найдено
     * @throws NoPermissionException если у пользователя нет разрешения на удаление данного объявления
     */
    public void deleteAnnouncement(User user, long announcementId) throws NoSuchElementException, NoPermissionException {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow();
        if (announcement.getUser().getId() != user.getId()) {
            throw new NoPermissionException("Wrong user");
        }
        announcementRepository.delete(announcement);
    }

    /**
     * Метод выполняет поиск объявлений по указанным параметрам.
     *
     * @param title заголовок объявления, используется для поиска объявлений с совпадающим заголовком; если null, поиск по заголовку не выполняется
     * @param username имя пользователя, используется для поиска объявлений, созданных указанным пользователем; если null, поиск по пользователю не выполняется
     * @param tag тег, используется для поиска объявлений, связанных с указанным тегом; если null, поиск по тегу не выполняется
     * @return список объектов AnnouncementResponseDto, соответствующих критериям поиска; если ничего не найдено, возвращается пустой список
     */
    public List<AnnouncementResponseDto> searchAnnouncement(String title, String username, String tag) {
        if (title != null) {
            return mapper.getAnnouncementResponseDtos(announcementRepository.findByTitle(title), false);
        } else if (username != null) {
            return mapper.getAnnouncementResponseDtos(announcementRepository.findByUsername(username), false);
        } else if (tag != null) {
            Tag tagEntity = tagRepository.findByName(tag.toLowerCase());
            if (tagEntity != null) {
                return mapper.getAnnouncementResponseDtos(announcementRepository.findByTag(tagEntity.getId()), false);
            }
        }
        return Collections.emptyList();
    }
}
