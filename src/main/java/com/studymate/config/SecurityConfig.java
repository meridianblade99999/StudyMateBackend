package com.studymate.config;

import com.studymate.filter.JwtFilter;
import com.studymate.handler.CustomAccessDeniedHandler;
import com.studymate.handler.CustomLogoutHandler;
import com.studymate.services.authentication.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Конфигурация безопасности
 */
@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true)
public class SecurityConfig {

    private final JwtFilter jwtFIlter;

    private final UserService userService;

    private final CustomAccessDeniedHandler accessDeniedHandler;

    private final CustomLogoutHandler customLogoutHandler;

    /**
     * Этот метод создает и настраивает цепочку фильтров безопасности для HTTP.
     *
     * @param http объект HttpSecurity для настройки цепочки фильтров безопасности
     * @return созданная цепочка фильтров безопасности
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);// Отключаем защиту от CSRF

        // Настраиваем авторизацию запросов
        http.authorizeHttpRequests(auth -> {
                    //auth.requestMatchers("/auth/**", "/**")
                    //        .permitAll(); // Разрешаем все запросы к этим URL
                    //auth.requestMatchers("/admin/**").hasAuthority("ADMIN"); // Разрешаем запросы только для администратора
                    //auth.anyRequest().authenticated(); // Требуем аутентификацию для всех остальных запросов
                    auth.anyRequest().permitAll();
                }).userDetailsService(userService)
                .exceptionHandling(e -> {
                    e.accessDeniedHandler(accessDeniedHandler); // Обработчик отказа доступа
                    e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)); // Входная точка аутентификации
                })
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS)) // Управление сессиями
                .addFilterBefore(jwtFIlter, UsernamePasswordAuthenticationFilter.class) // Добавление фильтра JWT перед фильтром UsernamePasswordAuthenticationFilter
                .logout(log -> {
                    log.logoutUrl("/api/logout"); // URL для выхода
                    log.addLogoutHandler(customLogoutHandler); // Добавление пользовательского обработчика выхода
                    log.logoutSuccessHandler((request, response, authentication) ->
                            SecurityContextHolder.clearContext()); // Очистка контекста безопасности после успешного выхода
                });

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}