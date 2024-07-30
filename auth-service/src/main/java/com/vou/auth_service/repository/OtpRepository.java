package com.vou.auth_service.repository;

import com.vou.auth_service.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OtpRepository extends JpaRepository<Otp, String> {
    List<Otp> findByPhone(String phone);
}
