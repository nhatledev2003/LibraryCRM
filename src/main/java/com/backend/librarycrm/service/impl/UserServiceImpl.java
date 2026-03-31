package com.backend.librarycrm.service.impl;

import com.backend.librarycrm.model.User;
import com.backend.librarycrm.repository.UserRepository;
import com.backend.librarycrm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    // Sau này sẽ tiêm thêm PasswordEncoder của Spring Security để mã hóa mật khẩu

    @Override
    public User registerReader(User user) {

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        user.setRole("READER");
        user.setActive(true);
        user.setFineBalance(0.0);

        // TODO: Mật khẩu cần được mã hóa (BCrypt) trước khi save ở các bước sau
        return userRepository.save(user);
    }

    @Override
    public User getUserProfile(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
    }
}