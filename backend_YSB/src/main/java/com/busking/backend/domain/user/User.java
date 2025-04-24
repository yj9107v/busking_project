package com.busking.backend.domain.user;

import com.busking.backend.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"socialId", "provider"})
})
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    private String username;

    @Column(length = 255)
    private String password;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 10, nullable = false, unique = true)
    private String nickname;

    @Column(length = 20, nullable = false)
    private String provider = "local";

    @Column(length = 50)
    private String socialId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'USER'")
    private Role role;

    @Column
    private Boolean isDeleted = false;

    @Column
    private LocalDateTime deletedAt;

    public enum Role {
        USER, ADMIN
    }
}