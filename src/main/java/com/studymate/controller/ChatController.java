package com.studymate.controller;

import com.studymate.annotations.Authorized;
import com.studymate.dto.chat.ChatResponseDto;
import com.studymate.dto.chat.ChatSettingsRequestDto;
import com.studymate.dto.chat.ChatSettingsResponseDto;
import com.studymate.entity.authentication.User;
import com.studymate.services.ChatService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

@Tag(name = "Контроллер чатов", description = "Отвечает за работу с чатами")
@AllArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final PageUtil pageUtil;
    private final ChatService chatService;

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получение списка чатов", description = "Получение списка чатов " +
        "авторизованного пользователя.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Возвращает список чатов",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ChatResponseDto.class))
            )
        )
    })
    @Authorized
    @GetMapping("/user")
    public ResponseEntity<List<ChatResponseDto>> getAllChats(@AuthenticationPrincipal User user, Integer page, Integer limit) {
        page = pageUtil.validatePageNumber(page);
        limit = pageUtil.validatePageSize(limit);
        return ResponseEntity.ok(chatService.getChatList(user, page, limit));
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получение информации о чате", description = "Получение информации о чате " +
        "для авторизованного пользователя. Сообщения чата разбиваются на страницы, " +
        "нумерация страниц начинается с 1")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Возвращает информацию о чате",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ChatResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Чат не найден",
            content = @Content()
        )
    })
    @Authorized
    @GetMapping("/{chatId}")
    public ResponseEntity<ChatResponseDto> getChat(
        @AuthenticationPrincipal User user,
        @PathVariable Long chatId,
        Integer page, Integer limit
    ) {
        page = pageUtil.validatePageNumber(page);
        limit = pageUtil.validatePageSize(limit);
        try {
            return ResponseEntity.ok(chatService.getChat(user, chatId, page, limit));
        } catch(NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Обновление настроек чата", description = "Обновление настроек чата " +
        "для авторизованного пользователя. Возвращает обновленные настройки.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Возвращает настройки чата",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ChatSettingsResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Чат не найден",
            content = @Content()
        )
    })
    @Authorized
    @PutMapping("/{chatId}/settings")
    public ResponseEntity<ChatSettingsResponseDto> updateChatSettings(
        @AuthenticationPrincipal User user,
        @PathVariable Long chatId,
        @RequestBody @Valid ChatSettingsRequestDto settingsRequestDto
    ) {
        try {
            return ResponseEntity.ok(chatService.updateChatSettings(user, chatId, settingsRequestDto));
        } catch(NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удаление чата", description = "Удаляет авторизованного пользователя из чата")
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Чат удален для пользователя",
            content = @Content()
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Чат не найден",
            content = @Content()
        )
    })
    @Authorized
    @DeleteMapping("/{chatId}")
    public ResponseEntity<?> deleteChat(@AuthenticationPrincipal User user, @PathVariable Long chatId) {
        try {
            chatService.deleteChat(user, chatId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch(NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
