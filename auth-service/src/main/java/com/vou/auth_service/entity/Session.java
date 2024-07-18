package com.vou.auth_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @ManyToOne(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "auth")
    private Auth auth;
}
