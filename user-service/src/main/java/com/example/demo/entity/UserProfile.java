package com.example.demo.entity;

import com.example.demo.enumerate.ProfileState;
import com.example.demo.enumerate.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
public class UserProfile {

    @Id
    @GeneratedValue(generator = "custom-id-generator")
    @GenericGenerator(name = "custom-id-generator", strategy = "com.example.demo.utils.CustomIdGenerator")
    private String userID;

    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String phone;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String nameOfUser;
    private boolean isActivate;
    private String birthday;
    private String avatar;
    private String gender;
    private String facebookAccount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProfileState state;
}
