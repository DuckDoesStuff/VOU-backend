package com.vou.api.entity;

import com.vou.api.enumerate.ProfileState;
import com.vou.api.enumerate.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
public class UserProfile {
    @Id
    @Column(updatable = false, nullable = false)
    private UUID userID;

    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String phone;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String displayName;
    private LocalDate birthday;
    private String avatar;
    private String gender;
    private String facebookAccount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProfileState state;
}
