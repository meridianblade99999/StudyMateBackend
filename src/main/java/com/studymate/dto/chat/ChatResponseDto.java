package com.studymate.dto.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatResponseDto {

    private Long id;
    private String name;

    private ChatResponseUserDto[] users;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("last_message")
    private ChatLastMessageDto lastMessage;

    @JsonProperty("chat_settings")
    private ChatSettingsResponseDto chatSettings;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ChatMessageResponseDto[] messages;

}
