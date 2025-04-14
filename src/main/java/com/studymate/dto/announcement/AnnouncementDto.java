package com.studymate.dto.announcement;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AnnouncementDto {

    @NotNull
    private String title;

    @NotNull
    private String description;

}
