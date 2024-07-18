package com.vou.auth_service.repository;

import com.vou.auth_service.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
    Auth findByUsername(String username);
}
