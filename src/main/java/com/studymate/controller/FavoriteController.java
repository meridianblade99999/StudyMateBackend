package com.studymate.controller;

import com.studymate.annotations.Authorized;
import com.studymate.dto.favorite.FavoriteCreateRequestDto;
import com.studymate.dto.favorite.FavoriteCreateResponseDto;
import com.studymate.entity.Favorite;
import com.studymate.entity.Response;
import com.studymate.entity.authentication.User;
import com.studymate.services.FavoriteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.naming.NoPermissionException;
import java.util.NoSuchElementException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Authorized
    @PostMapping("")
    public ResponseEntity addFavorite(@RequestBody @Valid FavoriteCreateRequestDto favoriteCreateRequestDto, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            favoriteService.createFavorite(user, favoriteCreateRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new FavoriteCreateResponseDto(user.getId(), favoriteCreateRequestDto.getAnnouncementId()));
        } catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Authorized
    @GetMapping("/user/{userId}")
    public ResponseEntity getAllByUserId(@PathVariable long userId, Integer page, Integer limit) {
        if (page == null || page < 1 || limit == null || limit < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().body(favoriteService.getUserFavorites(userId, page-1, limit));
    }

    @Authorized
    @DeleteMapping("/{favoriteId}")
    public ResponseEntity deleteFavorite(@PathVariable long favoriteId, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            favoriteService.deleteFavorite(user, favoriteId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch(NoPermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
