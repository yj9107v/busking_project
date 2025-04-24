package com.busking.backend.domain.postview;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "post_view", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"postId", "postType", "userId"})
})
public class PostView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostType postType;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime viewedAt;

    public enum PostType {
        PROMOTION, BOARD
    }
}
