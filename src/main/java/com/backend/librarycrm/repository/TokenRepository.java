package com.backend.librarycrm.repository;

import com.backend.librarycrm.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    // Tìm tất cả các token CÒN HỢP LỆ của 1 user (chưa bị revoke hoặc expire)
    @Query("SELECT t FROM Token t WHERE t.user.id = :userId AND (t.expired = false OR t.revoked = false)")
    List<Token> findAllValidTokenByUser(Integer userId);

    Optional<Token> findByToken(String token);
}