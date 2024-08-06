package com.example.demo.entity;


import com.example.demo.enumerate.ProfileState;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Data
public class BrandProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID brandID;

    @Column(unique = true, nullable = false)
    private String brandname;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String phone;

    private String displayName;
    private String domain;
    private String address;
    private Double lat;
    private Double lng;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProfileState state;
}
