package com.studymate.controller.authentication;

import com.studymate.dto.authentication.AuthenticationResponseDto;
import com.studymate.dto.authentication.LoginRequestDto;
import com.studymate.dto.authentication.RefreshTokenRequestDto;
import com.studymate.dto.authentication.RegistrationRequestDto;
import com.studymate.services.authentication.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Регистрация нового пользователя.
     *
     * @param registrationDto данные для регистрации
     * @return ответ о результате регистрации
     */
    @PostMapping("/registration")
    public ResponseEntity register(@RequestBody @Valid RegistrationRequestDto registrationDto) {
        try {
            authenticationService.register(registrationDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

        return ResponseEntity.ok(authenticationService.authenticate(new LoginRequestDto(registrationDto.getEmail(), registrationDto.getPassword())));
    }

    @PostMapping("/login")
    public ResponseEntity authenticate(@RequestBody LoginRequestDto request) {
        try {
            return ResponseEntity.ok(authenticationService.authenticate(request));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity refreshToken(HttpServletRequest request,
            HttpServletResponse response, @RequestBody @Valid RefreshTokenRequestDto refreshTokenRequestDto) {
        try {
            return authenticationService.refreshToken(request, response, refreshTokenRequestDto.getRefreshToken());
        } catch(UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}