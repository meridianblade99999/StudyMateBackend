package com.studymate.socket;

import com.corundumstudio.socketio.AuthorizationResult;
import com.corundumstudio.socketio.HandshakeData;
import com.studymate.entity.authentication.User;
import com.studymate.repository.authentication.UserRepository;
import com.studymate.services.authentication.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@AllArgsConstructor
@Component
public class SocketAuthorization {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public User authorize(HandshakeData data) {
        String token = data.getSingleUrlParam("token");
        if (token == null) {
            return null;
        }

        long userId;
        try {
            userId = jwtService.extractUserId(token);
        } catch (Exception e) {
            return null;
        }

        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            return null;
        }

        if (!jwtService.isValid(token, user.get())) {
            return null;
        }

        return user.get();
    }
}
