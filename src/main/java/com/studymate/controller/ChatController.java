package com.studymate.controller;

import com.studymate.annotations.Authorized;
import com.studymate.entity.authentication.User;
import com.studymate.services.ChatService;
import com.studymate.util.PageUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
