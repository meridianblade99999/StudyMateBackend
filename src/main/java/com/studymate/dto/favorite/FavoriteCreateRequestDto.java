package com.studymate.dto.favorite;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FavoriteCreateRequestDto {

    @JsonProperty("announcement_id")
    @NotNull
    private Long announcementId;

}
