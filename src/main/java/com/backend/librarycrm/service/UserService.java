package com.backend.librarycrm.service;

import com.backend.librarycrm.dto.request.UpdateProfileRequest;
import com.backend.librarycrm.model.User;

public interface UserService {
    User registerReader(User user);
    User getUserProfile(Integer userId);
    User getUserByUsername(String username);
    User updateProfile(String username, UpdateProfileRequest request);
    User payFine(String username, Double amount);
}