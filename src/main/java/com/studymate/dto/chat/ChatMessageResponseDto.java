package com.studymate.dto.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ChatMessageResponseDto {

    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("chat_id")
    private Long chatId;

    private String content;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("answered_message_id")
    private Long answeredMessageId;

    @JsonProperty("created_at")
    private Timestamp createdAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("updated_at")
    private Timestamp updatedAt;

}
