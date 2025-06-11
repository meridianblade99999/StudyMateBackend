package com.studymate.repository.authentication;

import com.studymate.entity.authentication.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);

    @Query("""
        select u.username from User u
        where lower(u.username) like %:username%
        order by u.id desc
        """)
    List<String> findUsers(Pageable pageable, String username);
}