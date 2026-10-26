package com.bagshop.controller;

import com.bagshop.dto.UserDto;
import com.bagshop.service.ProfileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public UserDto get() {
        return profileService.getProfile();
    }

    @PutMapping
    public UserDto update(@RequestBody UserDto dto) {
        return profileService.updateProfile(dto);
    }
}
