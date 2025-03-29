package com.studymate.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationResponseDto {

    @JsonProperty("access_token")
    @NotNull
    private String accessToken;

    @JsonProperty("refresh_token")
    @NotNull
    private String refreshToken;

}