package com.studymate.services;

import com.studymate.dto.announcement.AnnouncementResponseDto;
import com.studymate.dto.favorite.FavoriteCreateRequestDto;
import com.studymate.entity.Announcement;
import com.studymate.entity.Favorite;
import com.studymate.entity.authentication.User;
import com.studymate.repository.AnnouncementRepository;
import com.studymate.repository.FavoriteRepository;
import com.studymate.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.naming.NoPermissionException;
import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Класс FavoriteService предоставляет методы для управления избранными объявлениями пользователей.
 * Содержит логику для создания, получения и удаления избранных записей.
 *
 * Компонент работает с репозиториями FavoriteRepository и AnnouncementRepository,
 * а также использует вспомогательный объект MapperUtil для преобразования данных.
 */
@AllArgsConstructor
@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final AnnouncementRepository announcementRepository;
    private final MapperUtil mapper;

    /**
     * Создает новую запись в избранном для указанного пользователя и объявления.
     * Если указанное объявление уже находится в избранном, метод завершится без создания дубликата.
     *
     * @param user объект пользователя, для которого создается избранное
     * @param favoriteCreateRequestDto объект запроса, содержащий идентификатор объявления
     * @throws NoSuchElementException если объявление с указанным идентификатором не найдено
     */
    public void createFavorite(User user, FavoriteCreateRequestDto favoriteCreateRequestDto) throws NoSuchElementException {
        if (favoriteRepository.existsByUserIdAndAnnouncementId(user.getId(), favoriteCreateRequestDto.getAnnouncementId())) {
            return;
        }
        Announcement announcement = announcementRepository.findById(favoriteCreateRequestDto.getAnnouncementId()).orElseThrow();
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setAnnouncement(announcement);
        favorite.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        favoriteRepository.save(favorite);
    }

    /**
     * Возвращает список избранных объявлений пользователя с учетом постраничного вывода.
     *
     * @param userId идентификатор пользователя, для которого необходимо получить избранные объявления
     * @param page номер страницы (начиная с 0), который необходимо получить
     * @param pageSize количество записей на одну страницу
     * @return список объектов AnnouncementResponseDto, содержащих данные об избранных объявлениях
     */
    public List<AnnouncementResponseDto> getUserFavorites(long userId, int page, int pageSize) {
        List<Announcement> announcementList = announcementRepository.findLikeAnnouncements(userId, PageRequest.of(page, pageSize));
        return mapper.getAnnouncementResponseDtos(announcementList, false);
    }

    /**
     * Удаляет запись из избранных для указанного пользователя и объявления.
     * Если запись не найдена или пользователь не имеет прав доступа, происходит выброс исключения.
     *
     * @param user объект пользователя, чьи избранные записи нужно обновить
     * @param announcementId идентификатор объявления, которое требуется удалить из избранного
     * @throws NoSuchElementException если запись в избранном с указанными параметрами не найдена
     * @throws NoPermissionException если пользователь не имеет прав на удаление этой записи
     */
    public void deleteFavorite(User user, long announcementId) throws NoSuchElementException, NoPermissionException {
        Favorite favorite = favoriteRepository.findByUserIdAndAnnouncementId(user.getId(), announcementId).orElseThrow();
        if (favorite.getUser().getId() != user.getId()) {
            throw new NoPermissionException("Wrong user");
        }
        favoriteRepository.delete(favorite);
    }

}
