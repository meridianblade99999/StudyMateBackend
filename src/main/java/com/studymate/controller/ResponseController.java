package com.studymate.controller;

import com.studymate.annotations.Authorized;
import com.studymate.dto.response.CreateResponseDto;
import com.studymate.dto.response.CreateResponseRequestDto;
import com.studymate.dto.response.ResponseDto;
import com.studymate.entity.authentication.User;
import com.studymate.services.ResponseService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.naming.NoPermissionException;
import java.util.List;
import java.util.NoSuchElementException;

@Tag(name = "Контроллер откликов", description = "Отвечает за работу с откликами на объявления")
@AllArgsConstructor
@RestController
@RequestMapping("/api/response")
public class ResponseController {

    private final ResponseService responseService;

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Добавление избранного объявления", description = "Добавляет объявление в избранные " +
        "для авторизованного пользователя.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Отклик создан",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CreateResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Объявление, на которое создается отклик, не найдено",
            content = @Content()
        )
    })
    @Authorized
    @PostMapping("")
    public ResponseEntity<CreateResponseDto> createResponse(
        @RequestBody @Valid CreateResponseRequestDto createResponseRequestDto,
        @AuthenticationPrincipal User user
    ) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseService.createResponse(user, createResponseRequestDto));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Получение информации об отклике", description = "Позволяет получить " +
        "полную информацию об отклике по его id.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Возвращает информацию об отклике",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Отклик не найден",
            content = @Content()
        )
    })
    @GetMapping("/{responseId}")
    public ResponseEntity<ResponseDto> getResponse(@PathVariable long responseId) {
        try {
            return ResponseEntity.ok(responseService.getResponseById(responseId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Получение откликов на объявление", description = "Возвращает все отклики " +
        "на заданное объявление.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Возвращает все отклики на заданное объявление",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ResponseDto.class))
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Объявление не найдено",
            content = @Content()
        )
    })
    @GetMapping("/announcement/{announcementId}")
    public ResponseEntity<List<ResponseDto>> getAllResponseByAnnouncementId(@PathVariable long announcementId) {
        try {
            return ResponseEntity.ok(responseService.getResponsesByAnnouncementId(announcementId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Получение всех откликов пользователя", description = "Возвращает все отклики, " +
        "созданные заданным пользователям.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Возвращает все отклики пользователя",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ResponseDto.class))
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Объявление не найдено",
            content = @Content()
        )
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ResponseDto>> getAllReponseByUserId(@PathVariable long userId) {
        try {
            return ResponseEntity.ok(responseService.getResponsesByUserId(userId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удаление отклика", description = "Удаляет отклик авторизованного пользователя.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Отклик удален",
            content = @Content()
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Отклик не принадлежит пользователю",
            content = @Content()
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Отклик не найден",
            content = @Content()
        )
    })
    @Authorized
    @DeleteMapping("/{responseId}")
    public ResponseEntity<?> deleteResponse(@PathVariable long responseId, @AuthenticationPrincipal User user) {
        try {
            responseService.deleteResponse(user, responseId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch(NoPermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

}
