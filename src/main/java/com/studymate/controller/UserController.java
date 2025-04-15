package com.studymate.controller;

import com.studymate.annotations.Authorized;
import com.studymate.dto.user.UserUpdateDto;
import com.studymate.entity.authentication.User;
import com.studymate.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity getUserById(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(userService.getUserById(userId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Authorized
    @PutMapping("")
    public ResponseEntity updateUser(@Valid @RequestBody UserUpdateDto userUpdateDto, Authentication authentication) {
        userService.updateUser((User) authentication.getPrincipal(), userUpdateDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
