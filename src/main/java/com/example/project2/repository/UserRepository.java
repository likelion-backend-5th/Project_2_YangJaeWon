package com.example.project2.repository;

import com.example.project2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email); // 이메일 중복 가입 방지

    boolean existsByUsername(String username);
}
