package busking.busking_project.review;

import busking.busking_project.promotion.PromotionPost;
import busking.busking_project.user.User;
import busking.busking_project.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "review", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"promotion_post_id", "user_id"})
})
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 작성자(유저)와 N:1 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    // 홍보글(프로모션)과 N:1 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_post_id", nullable = false)
    private PromotionPost postId;

    @Column(nullable = false, length = 500)
    private String comment;

    @Column(nullable = false)
    private int rating;

    @Builder.Default
    @Column(nullable = false)
    private boolean isDeleted = false;

}