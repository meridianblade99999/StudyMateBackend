package com.studymate.controller.authentication;

import com.studymate.dto.announcement.AnnouncementResponseDto;
import com.studymate.dto.authentication.AuthenticationResponseDto;
import com.studymate.dto.authentication.LoginRequestDto;
import com.studymate.dto.authentication.RefreshTokenRequestDto;
import com.studymate.dto.authentication.RegistrationRequestDto;
import com.studymate.services.authentication.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Контроллер авторизации", description = "Отвечает за авторизацию")
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(
        summary = "Регистрация нового пользователя",
        description = "Создает нового пользователя, если в базе данных уже существует пользователь" +
            "с таким username или email, то возвращает код 409. В случае успешной регистрации возвращает" +
            "пару access и refresh токенов."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Пользователь создан",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthenticationResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Не удалось создать пользователя из-за конфликта в БД",
            content = @Content()
        )
    })
    @PostMapping("/registration")
    public ResponseEntity<AuthenticationResponseDto> register(
        @RequestBody @Valid RegistrationRequestDto registrationDto
    ) {
        try {
            authenticationService.register(registrationDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.ok(authenticationService
            .authenticate(new LoginRequestDto(registrationDto.getEmail(), registrationDto.getPassword())));
    }

    @Operation(
        summary = "Авторизация",
        description = "В случае успешной авторизации возвращает пару access и refresh токенов."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Авторизация успешна",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthenticationResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Неверные данные для авторизации",
            content = @Content()
        )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody LoginRequestDto request) {
        try {
            return ResponseEntity.ok(authenticationService.authenticate(request));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(
        summary = "Авторизация",
        description = "В случае успешной авторизации возвращает пару access и refresh токенов."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Токен обновлен",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthenticationResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Неверные данные для обновления",
            content = @Content()
        )
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(HttpServletRequest request,
            HttpServletResponse response, @RequestBody @Valid RefreshTokenRequestDto refreshTokenRequestDto) {
        try {
            return authenticationService.refreshToken(request, response, refreshTokenRequestDto.getRefreshToken());
        } catch(UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}