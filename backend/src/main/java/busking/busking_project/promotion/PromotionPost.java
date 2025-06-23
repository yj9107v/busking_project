package busking.busking_project.promotion;

import busking.busking_project.base.BaseEntity;
import busking.busking_project.user.User;
import busking.busking_project.review.Review;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "promotion_post")
public class PromotionPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 36)
    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId; // 작성자

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Lob
    private String mediaUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Builder.Default
    @Column(nullable = false)
    private Integer viewCount = 0;

    @Builder.Default
    private Boolean isDeleted = false;

    @Column(nullable = false, length = 255)
    private String place;

    public enum Category {
        ART, DANCE, MUSIC, TALK
    }

    // ✅ Review와의 양방향 연관관계 (1:N)
    @OneToMany(mappedBy = "promotionPostId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
}
