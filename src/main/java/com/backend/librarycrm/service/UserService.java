package com.backend.librarycrm.service;

import com.backend.librarycrm.model.User;

public interface UserService {
    User registerReader(User user);
    User getUserProfile(Integer userId);
}