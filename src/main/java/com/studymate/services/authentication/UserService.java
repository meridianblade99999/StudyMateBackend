package com.studymate.services.authentication;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    boolean existsByEmail(String email);

}