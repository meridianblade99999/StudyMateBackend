package com.studymate.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateResponseDto {

    @JsonProperty("description")
    private String description;

    @JsonProperty("announcement_id")
    private Long announcementId;

    @JsonProperty("user_id")
    private Long userId;

}
