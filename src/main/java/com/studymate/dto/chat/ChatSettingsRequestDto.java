package com.studymate.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChatSettingsRequestDto {

    @JsonProperty("muted")
    @NotNull
    private Boolean muted;

    @JsonProperty("notifications")
    @NotNull
    private Boolean notifications;

}
