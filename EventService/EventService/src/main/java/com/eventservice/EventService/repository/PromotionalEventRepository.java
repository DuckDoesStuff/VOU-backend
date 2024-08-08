package com.eventservice.EventService.repository;

import com.eventservice.EventService.entity.PromotionalEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PromotionalEventRepository extends JpaRepository<PromotionalEvent, Long> {
}
