package com.vou.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "brand_statistics")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandStatistics {
    @Id
    private String brandID;

    @Column(name = "total_events_hosted")
    private Long totalEventsHosted;
}
