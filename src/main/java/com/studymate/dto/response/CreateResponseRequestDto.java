package com.studymate.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateResponseRequestDto {

    @JsonProperty("description")
    @NotNull
    private String description;

    @JsonProperty("announcement_id")
    @NotNull
    private Long announcementId;

}
