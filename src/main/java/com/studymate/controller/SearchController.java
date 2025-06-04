package com.studymate.controller;

import com.studymate.dto.announcement.AnnouncementResponseDto;
import com.studymate.dto.response.ResponseDto;
import com.studymate.entity.authentication.User;
import com.studymate.services.SearchService;
import com.studymate.util.PageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Tag(name = "Контроллер поиска объявлений", description = "Отвечает за поиск объявлений")
@AllArgsConstructor
@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;
    private final PageUtil pageUtil;

    @Operation(
        summary = "Поиск",
        description = "Ищет по одному из параметров: title, username, tag"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Возвращает список найденных совпадений",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = String.class))
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Неверные параметры запроса"
        )
    })
    @GetMapping("")
    public ResponseEntity<List<String>> search(
        @RequestParam(required = false) String title,
        @RequestParam(name="user_name", required = false) String username,
        @RequestParam(required = false) String tag,
        Integer page, Integer limit
    ) {
        page = pageUtil.validatePageNumber(page);
        limit = pageUtil.validatePageSize(limit);
        if (title != null) {
            return ResponseEntity.ok(searchService.searchTitles(title));
        } else if (username != null) {
            return ResponseEntity.ok(searchService.searchUsernames(username));
        } else if (tag != null) {
            return ResponseEntity.ok(searchService.searchTags(tag));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
        summary = "Поиск по названию",
        description = "Ищет объявления по названию, поддерживает пагинацию"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Возвращает список найденных объявлений",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = AnnouncementResponseDto.class))
            )
        )
    })
    @GetMapping("/title/{title}")
    public ResponseEntity<List<AnnouncementResponseDto>> searchByTitle(
        @PathVariable String title, Integer page, Integer limit
    ) {
        page = pageUtil.validatePageNumber(page);
        limit = pageUtil.validatePageSize(limit);
        return ResponseEntity.ok(searchService.searchAnnouncementsByTitle(page, limit, title));
    }

    @Operation(
        summary = "Поиск по тегу",
        description = "Ищет объявления по тегу, поддерживает пагинацию"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Возвращает список найденных объявлений",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = AnnouncementResponseDto.class))
            )
        )
    })
    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<AnnouncementResponseDto>> searchByTag(
        @PathVariable String tag, Integer page, Integer limit
    ) {
        page = pageUtil.validatePageNumber(page);
        limit = pageUtil.validatePageSize(limit);
        return ResponseEntity.ok(searchService.searchAnnouncementsByTag(page, limit, tag));
    }

    @Operation(
        summary = "Поиск по имени пользователя",
        description = "Ищет ID пользователя по userName"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Возвращает ID пользователя",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Long.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден"
        )
    })
    @GetMapping("/user/{userName}")
    public ResponseEntity<Long> searchUserId(
        @PathVariable String userName
    ) {
        try {
            return ResponseEntity.ok(searchService.searchUserId(userName));
        } catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
