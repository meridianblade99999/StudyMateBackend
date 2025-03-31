package com.studymate.controller;

import com.studymate.annotations.Authorized;
import com.studymate.dto.response.CreateResponseDto;
import com.studymate.entity.authentication.User;
import com.studymate.services.ResponseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.naming.NoPermissionException;
import java.util.NoSuchElementException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/response")
public class ResponseController {

    private final ResponseService responseService;

    @Authorized
    @PostMapping("")
    public ResponseEntity createResponse(@RequestBody @Valid CreateResponseDto createResponseDto, Authentication authentication) {
        try {
            responseService.createResponse((User) authentication.getPrincipal(), createResponseDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createResponseDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("")
    public ResponseEntity getAllResponse() {
        return ResponseEntity.ok(responseService.getAllResponses());
    }

    @GetMapping("/{responseId}")
    public ResponseEntity getResponse(@PathVariable long responseId) {
        try {
            return ResponseEntity.ok(responseService.getResponseById(responseId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/announcement/{announcementId}")
    public ResponseEntity getAllResponseByAnnouncementId(@PathVariable long announcementId) {
        try {
            return ResponseEntity.ok(responseService.getResponsesByAnnouncementId(announcementId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity getAllReponseByUserId(@PathVariable long userId) {
        try {
            return ResponseEntity.ok(responseService.getResponsesByUserId(userId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Authorized
    @DeleteMapping("/{responseId}")
    public ResponseEntity deleteResponse(@PathVariable long responseId, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            responseService.deleteResponse(user, responseId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch(NoPermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

}
