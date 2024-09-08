package com.vou.api.repository;

import com.vou.api.entity.VoucherUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VoucherUserRepository extends JpaRepository<VoucherUser, Long> {
    List<VoucherUser> findByUserID(UUID userID);
}
