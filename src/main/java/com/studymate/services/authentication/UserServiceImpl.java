package com.studymate.services.authentication;

import com.studymate.entity.authentication.User;
import com.studymate.repository.authentication.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * Сервис для работы с пользователями.
 */

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

    @Override
    public boolean existsByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            return true;
        }
        return false;
    }

}