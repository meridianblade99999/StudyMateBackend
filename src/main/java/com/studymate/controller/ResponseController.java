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

import java.util.NoSuchElementException;

@AllArgsConstructor
@RestController
@RequestMapping("/response")
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
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity getAllReponseByUserId(@PathVariable long userId) {
        return ResponseEntity.ok().build();
    }

    @Authorized
    @DeleteMapping("/{responseId}")
    public ResponseEntity deleteResponse(@PathVariable long responseId) {
        return ResponseEntity.ok().build();
    }

}
