package com.studymate.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserResponseDto {

    @JsonProperty("id")
    private long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("user_name")
    private String username;

    @JsonProperty("description")
    private String description;

    @JsonProperty("location")
    private String location;

    @JsonProperty("gender")
    private Boolean gender;

    @JsonProperty("birthday")
    private Timestamp birthday;

    @JsonProperty("created_at")
    private Timestamp createdAt;

}
