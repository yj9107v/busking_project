package busking.busking_project.review;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "review", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"postId", "userId"})
})
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 500)
    private String comment;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime updatedAt;

    // ✅ @PrePersist로 createdAt 자동 설정
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    // ✅ 리뷰 복구 메소드 추가
    public void restore(String comment, int rating) {
        this.isDeleted = false;
        this.comment = comment;
        this.rating = rating;
        this.updatedAt = LocalDateTime.now();
    }

    // ✅ 리뷰 수정 메소드 추가
    public void update(String comment, int rating) {
        this.comment = comment;
        this.rating = rating;
        this.updatedAt = LocalDateTime.now();
    }

    // ✅ 리뷰 삭제 메소드 추가
    public void softDelete() {
        this.isDeleted = true;
        this.updatedAt = LocalDateTime.now();
    }

    // ✅ Builder 패턴 생성자 추가
    @Builder
    public Review(Long postId, Long userId, String comment, int rating, boolean isDeleted) {
        this.postId = postId;
        this.userId = userId;
        this.comment = comment;
        this.rating = rating;
        this.isDeleted = isDeleted;
        this.createdAt = LocalDateTime.now();  // 기본값 설정
    }
}

