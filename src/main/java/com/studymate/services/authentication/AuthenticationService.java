package com.studymate.services.authentication;

import com.studymate.dto.authentication.AuthenticationResponseDto;
import com.studymate.dto.authentication.AuthentificationResponseUserDto;
import com.studymate.dto.authentication.LoginRequestDto;
import com.studymate.dto.authentication.RegistrationRequestDto;
import com.studymate.entity.authentication.Token;
import com.studymate.entity.authentication.User;
import com.studymate.repository.authentication.TokenRepository;
import com.studymate.repository.authentication.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Сервис аутентификации и аутентификации пользователя.
 */
@AllArgsConstructor
@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final IUserService userService;

    /**
     * Регистрация нового пользователя.
     *
     * @param request запрос на регистрацию
     *
     */
    public void register(RegistrationRequestDto request) throws Exception {

        if(userService.existsByEmail(request.getEmail())) {
            throw new Exception("Email already exists");
        }

        if(userService.existsByUsername(request.getUsername())) {
            throw new Exception("Username already exists");
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        user = userRepository.save(user); // сохраняем пользователя в базе данных
    }


    /**
     * Авторизация пользователя.
     *
     * @param request объект с данными пользователя для авторизации
     * @return объект с токеном авторизации
     */
    public AuthenticationResponseDto authenticate(LoginRequestDto request) {
        // Поиск пользователя по имени пользователя
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        // Авторизация пользователя
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getId(),
                        request.getPassword()
                )
        );

        String accessToken = jwtService.generateAccessToken(user); // генерируем токен авторизации
        String refreshToken = jwtService.generateRefreshToken(user); // генерируем токен обновления

        revokeAllToken(user);

        saveUserToken(accessToken, refreshToken, user);

        // Возвращение объекта с токеном авторизации
        return new AuthenticationResponseDto(accessToken, refreshToken, new AuthentificationResponseUserDto(user.getId(), user.getName(), user.getUsername()));
    }

    /**
     * Метод отзывает все действительные токены для данного пользователя.
     *
     * @param user Пользователь, для которого нужно отменить токены.
     */
    private void revokeAllToken(User user) {
        List<Token> validTokens = tokenRepository.findAllAccessTokenByUser(user.getId());
        if(!validTokens.isEmpty()){
            validTokens.forEach(t ->{
                t.setLoggedOut(true);
            });
        }
        tokenRepository.saveAll(validTokens);
    }

    /**
     * Сохраняет токен авторизации пользователя в базе данных.
     *
     * @param accessToken Токен авторизации.
     * @param refreshToken Токен обновления.
     * @param user Информация о пользователе.
     */
    private void saveUserToken(String accessToken, String refreshToken, User user) {
        Token token = new Token();

        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setUser(user);

        tokenRepository.save(token);
    }

    /**
     * Обновляет токен аутентификации.
     *
     * @param request  HTTP-запрос.
     * @param response HTTP-ответ.
     * @return Ответ с обновленным токеном.
     */
    public ResponseEntity<AuthenticationResponseDto> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response,
            String token) {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        long userId = jwtService.extractUserId(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (jwtService.isValidRefresh(token, user)) {
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            revokeAllToken(user);

            saveUserToken(accessToken, refreshToken, user);

            return new ResponseEntity<>(new AuthenticationResponseDto(accessToken, refreshToken, new AuthentificationResponseUserDto(user.getId(), user.getName(), user.getUsername())), HttpStatus.OK);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}