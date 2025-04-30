package com.studymate.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResponseDto {

    private Long id;

    @JsonProperty("announcement_id")
    private Long announcementId;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("name")
    private String name;

    private String description;

}
