package com.studymate.dto.announcement;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AnnouncementCreateRequestDto {

    @Valid
    private AnnouncementDto announcement;

    @NotNull
    private String[] tags;

}
