package com.studymate.controller;

import com.studymate.annotations.Authorized;
import com.studymate.dto.user.UserResponseDto;
import com.studymate.dto.user.UserUpdateDto;
import com.studymate.entity.authentication.User;
import com.studymate.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Tag(name = "Контроллер пользователей", description = "Отвечает за работу с пользователями")
@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Получение данных о пользователе", description = "Получение данных о заданном пользователе.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Возвращает данные пользователя",
            content = @Content(schema = @Schema(implementation = UserResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден",
            content = @Content()
        )
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(userService.getUserById(userId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Обновление данных пользователя", description = "Обновляет личные данные " +
        "авторизованного пользователя.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Данные обновлены",
            content = @Content()
        )
    })
    @Authorized
    @PutMapping("")
    public ResponseEntity<?> updateUser(
        @Valid @RequestBody UserUpdateDto userUpdateDto,
        @AuthenticationPrincipal User user
    ) {
        userService.updateUser(user, userUpdateDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
