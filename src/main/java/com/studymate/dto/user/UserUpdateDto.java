package com.studymate.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class UserUpdateDto {

    @NotNull
    private String name;

    @NotNull
    private String location;

    private Boolean gender;

    private Timestamp birthday;

    @NotNull
    private String description;

}
