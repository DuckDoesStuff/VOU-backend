package com.vou.api.repository;

import com.vou.api.entity.PromotionalEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionEventRepository extends JpaRepository<PromotionalEvent, Long> {

}
