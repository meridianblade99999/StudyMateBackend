package com.studymate.filter;

import com.studymate.entity.authentication.User;
import com.studymate.repository.authentication.UserRepository;
import com.studymate.services.authentication.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    /**
     * Фильтр JWT, который проверяет JWT-токен в заголовке Authorization
     * и устанавливает аутентификацию пользователя, если токен валиден.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Получаем заголовок Authorization
        String authHeader = request.getHeader("Authorization");

        // Если заголовок не содержит JWT-токена, пропускаем фильтр
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Извлекаем JWT-токен из заголовка
        String token = authHeader.substring(7);

        // Если имя пользователя не пустое и аутентификация не установлена,
        // проверяем валидность токена и устанавливаем аутентификацию пользователя
        if(SecurityContextHolder.getContext().getAuthentication() == null) {

            long userId;

            try {
                userId = jwtService.extractUserId(token);
            } catch (Exception e) {
                filterChain.doFilter(request, response);
                return;
            }
            // Загружаем детали пользователя
            Optional<User> user = userRepository.findById(userId);
            if (!user.isEmpty()){
                // Проверяем валидность токена для данного пользователя
                if (jwtService.isValid(token, user.get())) {
                    // Создаем объект аутентификации с деталями пользователя
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            user.get(),
                            null,
                            user.get().getAuthorities()
                    );

                    // Устанавливаем детали аутентификации
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // Устанавливаем аутентификацию
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        // Пропускаем фильтр
        filterChain.doFilter(request, response);
    }

}