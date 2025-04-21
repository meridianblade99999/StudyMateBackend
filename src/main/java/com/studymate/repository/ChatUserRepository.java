package com.studymate.repository;

import com.studymate.entity.ChatUser;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {

    Optional<ChatUser> findById(Long id);
    List<ChatUser> findByChatId(Long chatId);
    List<ChatUser> findByUserId(Long userId, PageRequest pageRequest);
    boolean existsByChatIdAndUserId(Long chatId, Long userId);
    Optional<ChatUser> findByChatIdAndUserId(Long chatId, Long userId);

}
