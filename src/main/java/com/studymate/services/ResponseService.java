package com.studymate.services;

import com.studymate.dto.response.CreateResponseDto;
import com.studymate.entity.Announcement;
import com.studymate.entity.Response;
import com.studymate.entity.authentication.User;
import com.studymate.repository.AnnouncementRepository;
import com.studymate.repository.ResponseRepository;
import com.studymate.repository.authentication.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;

    public void createResponse(User user, CreateResponseDto createResponseDto) throws NoSuchElementException {
        Announcement announcement = announcementRepository.findById(createResponseDto.getAnnouncementId()).orElseThrow();
        Response response = new Response();
        response.setDescription(createResponseDto.getDescription());
        response.setAnnouncement(announcement);
        response.setUser(user);
        response.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        response = responseRepository.save(response);
        createResponseDto.setUserId(user.getId());
    }
}
