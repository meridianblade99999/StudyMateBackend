package com.studymate.dto.announcement;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AnnouncementCreateRequestDto {

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String[] tags;

}
