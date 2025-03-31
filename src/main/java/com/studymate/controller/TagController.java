package com.studymate.controller;

import com.studymate.annotations.Authorized;
import com.studymate.dto.tag.TagCreateDto;
import com.studymate.entity.authentication.User;
import com.studymate.services.ResponseService;
import com.studymate.services.TagService;
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
@RequestMapping("/tag")
public class TagController {

    private final TagService tagService;

    @Authorized
    @PostMapping("")
    public ResponseEntity createTag(@RequestBody @Valid TagCreateDto tagCreateDto) {
        tagService.createTag(tagCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(tagCreateDto);
    }

    @GetMapping("")
    public ResponseEntity getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @GetMapping("/{tagId}")
    public ResponseEntity getTagById(@PathVariable Long tagId) {
        try {
            return ResponseEntity.ok(tagService.getTag(tagId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Authorized
    @DeleteMapping("/{tagId}")
    public ResponseEntity deleteTagById(@PathVariable Long tagId , Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            tagService.deleteTag(user, tagId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
