package com.vou.api.repository;

import com.vou.api.entity.BrandStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandStatisticsRepository extends JpaRepository<BrandStatistics, String> {
    @Query("SELECT SUM(bs.totalEventsHosted) FROM brand_statistics bs")
    Long findTotalEventsHosted();
}
