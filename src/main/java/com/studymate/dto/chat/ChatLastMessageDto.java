package com.studymate.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ChatLastMessageDto {

    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("username")
    private String username;

    private String content;

    @JsonProperty("created_at")
    private Timestamp createdAt;

}
