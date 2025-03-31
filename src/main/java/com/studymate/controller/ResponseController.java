package com.studymate.controller;

import com.studymate.annotations.Authorized;
import com.studymate.services.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/response")
public class ResponseController {

    private final ResponseService responseService;

    @Authorized
    @PostMapping("")
    public ResponseEntity createResponse() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("")
    public ResponseEntity getAllResponse() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{responseId}")
    public ResponseEntity getResponse(@PathVariable long responseId) {
        return ResponseEntity.ok().build();
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
