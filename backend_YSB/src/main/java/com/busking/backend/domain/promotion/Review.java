package com.busking.backend.domain.promotion;

import com.busking.backend.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "review", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"postId", "userId"})
})
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, columnDefinition = "INT CHECK (rating BETWEEN 1 AND 5)")
    private Integer rating;

    @Lob
    private String comment;

    private Boolean isDeleted = false;

    public void update(int rating, String comment) {
        this.rating = rating;
        this.comment = comment;
        this.updatedAt = java.time.LocalDateTime.now();
    }
}
