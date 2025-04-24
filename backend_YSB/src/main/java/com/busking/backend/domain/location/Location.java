package com.busking.backend.domain.location;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(length = 50, nullable = false)
    private String region;

    @Lob
    private String description;

    @Column(nullable = false)
    private Boolean isActive = true;
}
