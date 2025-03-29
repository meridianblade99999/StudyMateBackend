package com.studymate.dto.favorite;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FavoriteCreateResponseDto {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("announcement_id")
    private Long announcementId;

}
