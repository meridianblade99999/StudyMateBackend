package com.studymate.repository;

import com.studymate.entity.Announcement;
import com.studymate.entity.Response;
import com.studymate.entity.authentication.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResponseRepository extends JpaRepository<Response, Long> {

    List<Response> findByAnnouncement(Announcement announcement);
    List<Response> findByUser(User user);

}
