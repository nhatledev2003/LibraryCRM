package com.backend.librarycrm.service.impl;

import com.backend.librarycrm.dto.request.UpdateProfileRequest;
import com.backend.librarycrm.model.User;
import com.backend.librarycrm.repository.UserRepository;
import com.backend.librarycrm.service.UserService;
import jakarta.transaction.Transactional;
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

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin người dùng!"));
    }

    @Override
    @Transactional
    public User updateProfile(String username, UpdateProfileRequest request) {
        User user = getUserByUsername(username);

        if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
            user.setFullName(request.getFullName());
        }
        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            user.setPhone(request.getPhone());
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User payFine(String username, Double amount) {
        User user = getUserByUsername(username);

        if (amount <= 0) {
            throw new RuntimeException("Số tiền đóng phạt phải lớn hơn 0");
        }
        if (user.getFineBalance() == 0) {
            throw new RuntimeException("Bạn không có khoản nợ phạt nào!");
        }

        // Trừ tiền phạt (nếu nộp dư thì trả về 0, không lưu số âm)
        double newBalance = user.getFineBalance() - amount;
        user.setFineBalance(Math.max(newBalance, 0.0));

        return userRepository.save(user);
    }
}