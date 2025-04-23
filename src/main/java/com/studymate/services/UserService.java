package com.studymate.services;

import com.studymate.dto.user.UserResponseDto;
import com.studymate.dto.user.UserUpdateDto;
import com.studymate.entity.authentication.User;
import com.studymate.repository.authentication.UserRepository;
import com.studymate.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/**
 * Сервисный класс для управления данными пользователя.
 * Предоставляет методы для получения информации о пользователях и обновления данных.
 */
@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;

    /**
     * Возвращает объект UserResponseDto, представляющий пользователя с указанным идентификатором.
     * Если пользователь с заданным идентификатором не найден, выбрасывается исключение NoSuchElementException.
     *
     * @param id уникальный идентификатор пользователя
     * @return объект UserResponseDto, содержащий информацию о пользователе
     * @throws NoSuchElementException если пользователь с указанным идентификатором не найден
     */
    public UserResponseDto getUserById(Long id) throws NoSuchElementException {
        return mapperUtil.toUserResponseDto(userRepository.findById(id).orElseThrow());
    }

    /**
     * Обновляет данные пользователя на основе предоставленного объекта UserUpdateDto
     * и сохраняет изменения в базе данных.
     *
     * @param user объект User, данные которого будут обновлены
     * @param updateDto объект UserUpdateDto, содержащий новые данные для обновления
     *                  пользователя
     */
    public void updateUser(User user, UserUpdateDto updateDto) {
        user.setName(updateDto.getName());
        user.setLocation(updateDto.getLocation());
        user.setGender(updateDto.getGender());
        user.setBirthday(updateDto.getBirthday());
        user.setDescription(updateDto.getDescription());
        userRepository.save(user);
    }

}
