package com.studymate.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateResponseDto {

    @JsonProperty("description")
    @NotNull
    private String description;

    @JsonProperty("announcement_id")
    @NotNull
    private Long announcementId;

    @JsonProperty("user_id")
    private Long userId;

}
