package com.vou.auth_service.repository;

import com.vou.auth_service.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Session findOneByRefreshToken(String refreshToken);
}
