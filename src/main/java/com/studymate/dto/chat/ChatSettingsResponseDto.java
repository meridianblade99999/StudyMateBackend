package com.studymate.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatSettingsResponseDto {

    private Long id;
    private Boolean muted;
    private Boolean notifications;

}
