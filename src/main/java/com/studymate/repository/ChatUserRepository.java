package com.studymate.repository;

import com.studymate.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
}
