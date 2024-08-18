package com.vou.api.repository;

import com.vou.api.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OtpRepository extends JpaRepository<Otp, String> {
    List<Otp> findByPhone(String phone);
}
