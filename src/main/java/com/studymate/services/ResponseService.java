package com.studymate.services;

import com.studymate.dto.response.CreateResponseDto;
import com.studymate.dto.response.ResponseDto;
import com.studymate.entity.Announcement;
import com.studymate.entity.Response;
import com.studymate.entity.authentication.User;
import com.studymate.repository.AnnouncementRepository;
import com.studymate.repository.ResponseRepository;
import com.studymate.repository.authentication.UserRepository;
import com.studymate.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.naming.NoPermissionException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Сервисный класс для выполнения операций, связанных с сущностью Response.
 * Предоставляет методы для создания, получения, фильтрации и удаления ответов.
 *
 * Компонент аннотирован как @Service, что позволяет использовать его в качестве
 * Spring Bean.
 */
@AllArgsConstructor
@Service
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;

    /**
     * Создает новый отклик на объявление на основе переданных данных.
     *
     * @param user пользователь, создающий отклик
     * @param createResponseDto объект, содержащий информацию для создания отклика,
     *                          включая идентификатор объявления и описание отклика
     * @throws NoSuchElementException если объявление с указанным идентификатором не найдено
     */
    public void createResponse(User user, CreateResponseDto createResponseDto) throws NoSuchElementException {
        Announcement announcement = announcementRepository.findById(createResponseDto.getAnnouncementId()).orElseThrow();
        Response response = new Response();
        response.setDescription(createResponseDto.getDescription());
        response.setAnnouncement(announcement);
        response.setUser(user);
        response.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        response = responseRepository.save(response);
        createResponseDto.setUserId(user.getId());
    }

    /**
     * Метод возвращает список всех ответов, хранящихся в базе данных.
     *
     * @return список объектов типа ResponseDto, содержащих данные о всех ответах
     */
    public List<ResponseDto> getAllResponses() {
        List<Response> responses = responseRepository.findAll();
        List<ResponseDto> responseDtos = new ArrayList<>(responses.size());
        for (Response response : responses) {
            responseDtos.add(mapperUtil.toResponseDto(response));
        }
        return responseDtos;
    }

    /**
     * Возвращает объект ResponseDto по указанному идентификатору.
     *
     * @param id идентификатор ответа, который необходимо получить
     * @return объект ResponseDto, содержащий данные ответа
     * @throws NoSuchElementException если объект Response с указанным идентификатором не найден
     */
    public ResponseDto getResponseById(Long id) throws NoSuchElementException {
        Response response = responseRepository.findById(id).orElseThrow();
        return mapperUtil.toResponseDto(response);
    }

    /**
     * Возвращает список ответов, связанных с определенным объявлением.
     *
     * @param announcementId идентификатор объявления, для которого необходимо найти ответы
     * @return список объектов ResponseDto, представляющих ответы на объявление
     * @throws NoSuchElementException если объявление с указанным идентификатором не найдено
     */
    public List<ResponseDto> getResponsesByAnnouncementId(Long announcementId) throws NoSuchElementException {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow();
        List<Response> responses = responseRepository.findByAnnouncement(announcement);
        List<ResponseDto> responseDtos = new ArrayList<>(responses.size());
        for (Response response : responses) {
            responseDtos.add(mapperUtil.toResponseDto(response));
        }
        return responseDtos;
    }

    /**
     * Возвращает список откликов, созданных пользователем с указанным идентификатором.
     *
     * @param userId идентификатор пользователя, для которого необходимо получить список откликов
     * @return список объектов типа ResponseDto, представляющих отклики пользователя
     * @throws NoSuchElementException если пользователь с указанным идентификатором не найден
     */
    public List<ResponseDto> getResponsesByUserId(Long userId) throws NoSuchElementException {
        User user = userRepository.findById(userId).orElseThrow();
        List<Response> responses = responseRepository.findByUser(user);
        List<ResponseDto> responseDtos = new ArrayList<>(responses.size());
        for (Response response : responses) {
            responseDtos.add(mapperUtil.toResponseDto(response));
        }
        return responseDtos;
    }

    /**
     * Удаляет отклик из базы данных на основе предоставленного идентификатора.
     * Проверяет, принадлежит ли отклик указанному пользователю.
     *
     * @param user пользователь, который пытается выполнить удаление отклика
     * @param favoriteId идентификатор отклика, который нужно удалить
     * @throws NoSuchElementException если отклик с указанным идентификатором не найден
     * @throws NoPermissionException если отклик не принадлежит указанному пользователю
     */
    public void deleteResponse(User user, long favoriteId) throws NoSuchElementException, NoPermissionException {
        Response response = responseRepository.findById(favoriteId).orElseThrow();
        if (response.getUser().getId() != user.getId()) {
            throw new NoPermissionException("Wrong user");
        }
        responseRepository.delete(response);
    }

}
