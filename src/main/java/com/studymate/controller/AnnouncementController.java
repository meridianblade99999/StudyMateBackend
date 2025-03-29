package com.studymate.controller;

import com.studymate.annotations.Authorized;
import com.studymate.dto.announcement.AnnouncementCreateRequestDto;
import com.studymate.dto.announcement.AnnouncementResponseDto;
import com.studymate.dto.announcement.AnnouncementUpdateDto;
import com.studymate.entity.Announcement;
import com.studymate.entity.authentication.User;
import com.studymate.services.AnnouncementService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.naming.NoPermissionException;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @Authorized
    @PostMapping("")
    public ResponseEntity create(@RequestBody @Valid AnnouncementCreateRequestDto requestDto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Announcement announcement = announcementService.create(user, requestDto.getAnnouncement());
        if (announcement == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        for (String tag : requestDto.getTags()) {
            announcementService.addTag(announcement, tag);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("")
    public ResponseEntity getAllAnnouncements(Integer page, Integer limit) {
        if (page == null || page < 1 || limit == null || limit < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<AnnouncementResponseDto> announcementList = announcementService.getAnnouncements(page-1, limit);
        return ResponseEntity.ok(announcementList);
    }

    @GetMapping("/short-info/{announcementId}")
    public ResponseEntity getShortAnnouncement(@PathVariable long announcementId) {
        try {
            return ResponseEntity.ok(announcementService.getShortAnouncement(announcementId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/full-info/{announcementId}")
    public ResponseEntity getFullAnnouncement(@PathVariable long announcementId, Authentication authentication) {
        User user = authentication != null ? (User) authentication.getPrincipal() : null;
        try {
            return ResponseEntity.ok(announcementService.getFullAnnouncement(announcementId, user));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity getAllByUserId(@PathVariable long userId, Integer page, Integer limit) {
        if (page == null || page < 1 || limit == null || limit < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<AnnouncementResponseDto> announcementList = announcementService.getUserAnnouncements(userId, page-1, limit);
        return ResponseEntity.ok(announcementList);
    }

    @Authorized
    @PutMapping("/{announcementId}")
    public ResponseEntity update(@PathVariable long announcementId, Authentication authentication, @RequestBody @Valid AnnouncementUpdateDto updateDto) {
        try {
            User user = (User) authentication.getPrincipal();
            announcementService.updateAnnouncement(user, announcementId, updateDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(announcementService.getFullAnnouncement(announcementId, user));
        } catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (NoPermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Authorized
    @DeleteMapping("/{announcementId}")
    public ResponseEntity deleteById(@PathVariable long announcementId, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            announcementService.deleteAnnouncement(user, announcementId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (NoPermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

}
