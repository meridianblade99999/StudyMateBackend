package com.studymate.services.authentication;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Интерфейс IUserService предоставляет методы для работы с пользователями.
 * Наследуется от интерфейса UserDetailsService, что позволяет использовать
 * метод для загрузки пользовательских данных по имени.
 */
public interface IUserService extends UserDetailsService {

    /**
     * Проверяет наличие пользователя с указанным адресом электронной почты.
     *
     * @param email адрес электронной почты, который необходимо проверить на существование
     * @return true, если пользователь с таким адресом электронной почты существует, в противном случае false
     */
    boolean existsByEmail(String email);

    /**
     * Проверяет, существует ли пользователь с указанным именем.
     *
     * @param username имя пользователя, наличие которого нужно проверить
     * @return true, если пользователь с указанным именем существует, в противном случае false
     */
    boolean existsByUsername(String username);

}