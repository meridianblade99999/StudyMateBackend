package com.studymate.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RefreshTokenRequestDto {

    @JsonProperty("refresh_token")
    @NotNull
    private String refreshToken;

}
