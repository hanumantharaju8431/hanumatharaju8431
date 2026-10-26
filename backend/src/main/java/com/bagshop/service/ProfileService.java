package com.bagshop.service;

import com.bagshop.dto.UserDto;
import com.bagshop.entity.User;
import com.bagshop.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public ProfileService(UserRepository userRepository, CurrentUserService currentUserService) {
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    public UserDto getProfile() {
        return UserDto.from(currentUserService.requireUser());
    }

    @Transactional
    public UserDto updateProfile(UserDto dto) {
        User user = currentUserService.requireUser();
        if (dto.getName() != null && !dto.getName().isBlank()) {
            user.setName(dto.getName());
        }
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        return UserDto.from(userRepository.save(user));
    }
}
