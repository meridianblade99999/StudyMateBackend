package com.studymate.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthentificationResponseUserDto {

    private long id;
    private String name;
    private String username;

}
