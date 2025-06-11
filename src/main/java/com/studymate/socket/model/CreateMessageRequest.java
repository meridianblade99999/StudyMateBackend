package com.studymate.socket.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateMessageRequest {

    @NotNull
    @JsonProperty("chat_id")
    private Long chatId;

    @JsonProperty("answered_message_id")
    private Long answeredMessageId;

    @NotNull
    private String content;
}
