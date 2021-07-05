package com.rest.api.repo;

import com.rest.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepo extends JpaRepository<User, Long> {
    
    // 회원 가입 시 가입한 이메을 조회를 위하여 추가
    Optional<User> findByUid(String email);
}