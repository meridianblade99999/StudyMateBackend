package com.studymate.services;

import com.studymate.dto.user.UserResponseDto;
import com.studymate.dto.user.UserUpdateDto;
import com.studymate.entity.authentication.User;
import com.studymate.repository.authentication.UserRepository;
import com.studymate.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;

    public UserResponseDto getUserById(Long id) throws NoSuchElementException {
        return mapperUtil.toUserResponseDto(userRepository.findById(id).orElseThrow());
    }

    public void updateUser(User user, UserUpdateDto updateDto) {
        user.setName(updateDto.getName());
        user.setLocation(updateDto.getLocation());
        user.setGender(updateDto.getGender());
        user.setBirthday(updateDto.getBirthday());
        user.setDescription(updateDto.getDescription());
        userRepository.save(user);
    }

}
