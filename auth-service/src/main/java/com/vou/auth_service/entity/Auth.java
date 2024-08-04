package com.vou.auth_service.entity;

import com.vou.auth_service.enumerate.ProfileState;
import com.vou.auth_service.enumerate.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Auth {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'USER'")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'PENDING'")
    private ProfileState profileState;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
    @Column(name = "session_id")
    private Set<Session> sessions;
}
