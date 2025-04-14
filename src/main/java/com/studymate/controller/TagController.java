package com.studymate.controller;

import com.studymate.services.TagService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.NoPermissionException;
import java.util.NoSuchElementException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/tag")
public class TagController {

    private final TagService tagService;

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

}
