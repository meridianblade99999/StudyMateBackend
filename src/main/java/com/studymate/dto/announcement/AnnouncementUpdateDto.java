package com.studymate.dto.announcement;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AnnouncementUpdateDto {

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String[] tags;

}
