package com.studymate.repository;

import com.studymate.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Optional<Message> findFirstByChatIdOrderByIdDesc(Long chatId);
    List<Message> findByChatIdOrderByIdDesc(long chatId, Pageable pageable);

}
