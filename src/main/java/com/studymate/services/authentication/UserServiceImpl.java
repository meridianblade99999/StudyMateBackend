package com.studymate.services.authentication;

import com.studymate.entity.authentication.User;
import com.studymate.repository.authentication.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * Сервис для работы с пользователями.
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    /**
     * Метод загружает пользователя по его имени.
     * Если пользователь не найден, выбрасывает исключение UsernameNotFoundException.
     *
     * @param id - айди пользователя для поиска
     * @return найденный пользователь
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return userRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new UsernameNotFoundException("User " + id + " not found"));
    }

    /**
     * Проверяет, существует ли пользователь с данным email.
     *
     * @param email - адрес электронной почты пользователя, который требуется проверить
     * @return true, если пользователь с данным email существует, в противном случае false
     */
    @Override
    public boolean existsByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            return true;
        }
        return false;
    }

    /**
     * Проверяет, существует ли пользователь с данным именем пользователя.
     *
     * @param username - имя пользователя, которое требуется проверить
     * @return true, если пользователь с данным именем существует, в противном случае false
     */
    @Override
    public boolean existsByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            return true;
        }
        return false;
    }

}