package busking.busking_project.user;

import busking.busking_project.promotion.PromotionPost;
import busking.busking_project.review.Review;
import busking.busking_project.board.BoardPost;
import busking.busking_project.comment.Comment;
import busking.busking_project.Busking_Schedule.BuskingSchedule;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity // JPA 엔티티임을 명시
@Table(name = "users") // DB 테이블 이름을 "users"로 지정
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String username;

    @Column(length = 255)
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, unique = true, length = 30)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AuthProvider provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Builder.Default
    @Column(nullable = false)
    private boolean isDeleted = false;

    private LocalDateTime deletedAt;

    @Column(name = "social_id", unique = true)
    private String socialId;

    // ✅ 양방향 연관관계 추가

    // 1:N : User -> PromotionPost
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PromotionPost> promotionPosts = new ArrayList<>();

    // 1:N : User -> Review
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    // 1:N : User -> BoardPost
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardPost> boardPosts = new ArrayList<>();

    // 1:N : User -> Comment
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // 1:N : User -> BuskingSchedule
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BuskingSchedule> buskingSchedules = new ArrayList<>();

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
