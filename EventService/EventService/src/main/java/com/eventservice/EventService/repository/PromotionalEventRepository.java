package com.eventservice.EventService.repository;

import com.eventservice.EventService.entity.PromotionalEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PromotionalEventRepository extends JpaRepository<PromotionalEvent, Long> {
    @Query(value = "SELECT eventID FROM promotional_event WHERE brandID = :brandID", nativeQuery = true)
    List<Long> getEventsIDByBrandID(Long brandID);

    List<PromotionalEvent> findByBrandID(Long brandID);
}
