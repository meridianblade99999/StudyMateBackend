package com.studymate.dto.tag;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TagCreateDto {

    @NotNull
    public String name;

}
