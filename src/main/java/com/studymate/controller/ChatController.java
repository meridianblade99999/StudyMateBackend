package com.studymate.controller;

import com.studymate.annotations.Authorized;
import com.studymate.dto.chat.ChatSettingsRequestDto;
import com.studymate.entity.authentication.User;
import com.studymate.services.ChatService;
import com.studymate.util.PageUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final PageUtil pageUtil;
    private final ChatService chatService;

    @Authorized
    @GetMapping("")
    public ResponseEntity getAllChats(Authentication authentication, Integer page, Integer limit) {
        page = pageUtil.validatePageNumber(page);
        limit = pageUtil.validatePageSize(limit);
        return ResponseEntity.ok(chatService.getChatList((User) authentication.getPrincipal(), page-1, limit));
    }

    @Authorized
    @GetMapping("/{chatId}")
    public ResponseEntity getChat(Authentication authentication, @PathVariable Long chatId,
                                  Integer page, Integer limit) {
        page = pageUtil.validatePageNumber(page);
        limit = pageUtil.validatePageSize(limit);
        try {
            return ResponseEntity.ok(chatService.getChat((User) authentication.getPrincipal(), chatId, page-1, limit));
        } catch(NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Authorized
    @PutMapping("/{chatId}/settings")
    public ResponseEntity updateChatSettings(Authentication authentication, @PathVariable Long chatId,
                                   @RequestBody @Valid ChatSettingsRequestDto settingsRequestDto) {
        try {
            return ResponseEntity.ok(chatService.updateChatSettings((User) authentication.getPrincipal(), chatId, settingsRequestDto));
        } catch(NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Authorized
    @DeleteMapping("/{chatId}")
    public ResponseEntity deleteChat(Authentication authentication, @PathVariable Long chatId) {
        try {
            chatService.deleteChat((User) authentication.getPrincipal(), chatId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch(NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
