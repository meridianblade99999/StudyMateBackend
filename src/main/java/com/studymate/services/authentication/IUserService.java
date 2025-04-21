package com.studymate.services.authentication;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

}