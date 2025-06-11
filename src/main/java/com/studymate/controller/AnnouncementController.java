package com.studymate.controller;

import com.studymate.annotations.Authorized;
import com.studymate.dto.announcement.AnnouncementCreateRequestDto;
import com.studymate.dto.announcement.AnnouncementResponseDto;
import com.studymate.dto.announcement.AnnouncementUpdateDto;
import com.studymate.entity.Announcement;
import com.studymate.entity.authentication.User;
import com.studymate.services.AnnouncementService;
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
import javax.naming.NoPermissionException;
import java.util.List;
import java.util.NoSuchElementException;

@Tag(name = "Контроллер объявлений", description = "Отвечает за работу с объявлениями")
@AllArgsConstructor
@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

    private final AnnouncementService announcementService;
    private final PageUtil pageUtil;

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Создание объявления", description = "Создает новое объявление")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Объявление создано", content = @Content()),
        @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content())
    })
    @Authorized
    @PostMapping("")
    public ResponseEntity<AnnouncementResponseDto> create(
        @RequestBody @Valid AnnouncementCreateRequestDto requestDto,
        @AuthenticationPrincipal User user
    ) {
        Announcement announcement = announcementService.create(user, requestDto);
        if (announcement == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        for (String tag : requestDto.getTags()) {
            announcementService.addTag(announcement, tag);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
        summary = "Получение списка объявлений",
        description = "Возвращает объявления с заданными параметрами, " +
            "поддерживает разбиение на страницы, нумерация страниц начинается с 1"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Возвращает список объявлений, " +
                "если ничего не найдено, то возвращает пустой список",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = AnnouncementResponseDto.class))
            )
        )
    })
    @GetMapping("")
    public ResponseEntity<List<AnnouncementResponseDto>> getAllAnnouncements(
        Integer page, Integer limit, String tags, Boolean gender,
        @RequestParam(name="min_age", required = false) Integer minAge,
        @RequestParam(name="max_age", required = false) Integer maxAge
    ) {
        page = pageUtil.validatePageNumber(page);
        limit = pageUtil.validatePageSize(limit);
        List<AnnouncementResponseDto> announcementList = announcementService
            .getAnnouncements(page, limit, tags, gender, minAge, maxAge);
        return ResponseEntity.ok(announcementList);
    }

    @Operation(
            summary = "Получение короткой версии объявления",
            description = "Возвращает короткую версию объявления без его содержимого"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Возвращает объявление",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation =
                    AnnouncementResponseDto.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Объявление с искомым id не найдено", content = @Content())
    })
    @GetMapping("/short-info/{announcementId}")
    public ResponseEntity<AnnouncementResponseDto> getShortAnnouncement(@PathVariable long announcementId) {
        try {
            return ResponseEntity.ok(announcementService.getShortAnouncement(announcementId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(
        summary = "Получение полной версии объявления",
        description = "Возвращает полную версию объявления"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Возвращает объявление",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation =
                    AnnouncementResponseDto.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Объявление с искомым id не найдено", content = @Content())
    })
    @GetMapping("/full-info/{announcementId}")
    public ResponseEntity<AnnouncementResponseDto> getFullAnnouncement(
        @PathVariable long announcementId, @AuthenticationPrincipal User user
    ) {
        try {
            return ResponseEntity.ok(announcementService.getFullAnnouncement(announcementId, user));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(
        summary = "Получение всех объявлений пользователя",
        description = "Возвращает все объявления пользователя без их содержимого. " +
            "Поддерживает разбиение на страницы, нумерация страниц начинается с 1."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Возвращает объявления пользователя",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation =
                    AnnouncementResponseDto.class)
            )
        )
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AnnouncementResponseDto>> getAllByUserId(
        @PathVariable long userId,
        Integer page, Integer limit
    ) {
        page = pageUtil.validatePageNumber(page);
        limit = pageUtil.validatePageSize(limit);
        return ResponseEntity.ok(announcementService.getUserAnnouncements(userId, page, limit));
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
        summary = "Изменение объявление",
        description = "Изменяет объявление. Объявление должно принадлежать авторизованному пользователю"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Объявление обновлено, возвращает новую версию объявления",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation =
                    AnnouncementResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Объявление не принадлежит авторизованному пользователю",
            content = @Content()
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Объявление не найдено",
            content = @Content()
        )
    })
    @Authorized
    @PutMapping("/{announcementId}")
    public ResponseEntity<AnnouncementResponseDto> update(
        @PathVariable long announcementId,
        @AuthenticationPrincipal User user,
        @RequestBody @Valid AnnouncementUpdateDto updateDto
    ) {
        try {
            announcementService.updateAnnouncement(user, announcementId, updateDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(announcementService.getFullAnnouncement(announcementId, user));
        } catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (NoPermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
        summary = "Удаляет объявление",
        description = "Удаляет объявление. Объявление должно принадлежать авторизованному пользователю"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Объявление удалено",
            content = @Content()
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Объявление не принадлежит авторизованному пользователю",
            content = @Content()
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Объявление не найдено",
            content = @Content()
        )
    })
    @Authorized
    @DeleteMapping("/{announcementId}")
    public ResponseEntity<AnnouncementResponseDto> deleteById(
        @PathVariable long announcementId,
        @AuthenticationPrincipal User user
    ) {
        try {
            announcementService.deleteAnnouncement(user, announcementId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (NoPermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

}
