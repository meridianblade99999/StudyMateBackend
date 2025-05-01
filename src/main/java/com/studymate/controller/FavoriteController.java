package com.studymate.controller;

import com.studymate.annotations.Authorized;
import com.studymate.dto.announcement.AnnouncementResponseDto;
import com.studymate.dto.favorite.FavoriteCreateRequestDto;
import com.studymate.dto.favorite.FavoriteCreateResponseDto;
import com.studymate.entity.authentication.User;
import com.studymate.services.FavoriteService;
import com.studymate.util.PageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import java.util.List;
import java.util.NoSuchElementException;

@Tag(name = "Контроллер избранного", description = "Отвечает за работу с избранными объявлениями")
@AllArgsConstructor
@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final PageUtil pageUtil;

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Добавление избранного объявления", description = "Добавляет объявление в избранные " +
        "для авторизованного пользователя.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Возвращает список чатов",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FavoriteCreateResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Объявление не найдено",
            content = @Content()
        )
    })
    @Authorized
    @PostMapping("")
    public ResponseEntity<FavoriteCreateResponseDto> addFavorite(
        @RequestBody @Valid FavoriteCreateRequestDto favoriteCreateRequestDto,
        @AuthenticationPrincipal User user
    ) {
        try {
            favoriteService.createFavorite(user, favoriteCreateRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new FavoriteCreateResponseDto(user.getId(), favoriteCreateRequestDto.getAnnouncementId()));
        } catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получение избранных объявлений", description = "Возвращает список избранных объявлений " +
        "для авторизованного пользователя. Поддерживает разбиение на страницы, нумерация страниц начинается с 1.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Возвращает список избранных объявлений",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = AnnouncementResponseDto.class))
            )
        )
    })
    @Authorized
    @GetMapping("/user")
    public ResponseEntity<List<AnnouncementResponseDto>> getAllByUserId(
        @PathVariable long userId,
        @AuthenticationPrincipal User user,
        Integer page, Integer limit
    ) {
        page = pageUtil.validatePageNumber(page);
        limit = pageUtil.validatePageSize(limit);
        return ResponseEntity.ok().body(favoriteService.getUserFavorites(user, page, limit));
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удаление избранного", description = "Удаляет объявление из избранного для " +
        "авторизованного пользователя.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Объявление удалено из избранного",
            content = @Content()
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Объявление не найдено в избранном",
            content = @Content()
        )
    })
    @Authorized
    @DeleteMapping("/{announcementId}")
    public ResponseEntity deleteFavorite(@PathVariable long announcementId, @AuthenticationPrincipal User user) {
        try {
            favoriteService.deleteFavorite(user, announcementId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
