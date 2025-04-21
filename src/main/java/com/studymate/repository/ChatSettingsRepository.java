package com.studymate.repository;

import com.studymate.entity.ChatSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSettingsRepository extends JpaRepository<ChatSettings, Long> {
}
