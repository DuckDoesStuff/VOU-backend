package com.vou.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "game_statistics")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameStatistics {
    @Id
    private String gameID;
    private String gameType;
    private Long totalParticipants;
}
