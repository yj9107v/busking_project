package busking.busking_project.review;

import busking.busking_project.promotion.PromotionPost;
import busking.busking_project.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Review JPA Repository
 * - PromotionPost, User 객체 참조 기반 메서드 사용
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * 특정 홍보글에 대한 전체 리뷰(Soft Delete 미포함)
     */
    List<Review> findByPostIdAndIsDeletedFalse(PromotionPost postId);

    /**
     * 특정 홍보글의 한 사용자의 리뷰(Soft Delete 미포함)
     */
    Optional<Review> findByPostIdAndUserIdAndIsDeletedFalse(PromotionPost postId, User userId);

    /**
     * 특정 홍보글+사용자 조합의 리뷰 존재여부(Soft Delete 미포함)
     * - 중복 리뷰 작성 방지용
     */
    boolean existsByPostIdAndUserIdAndIsDeletedFalse(PromotionPost postId, User userId);

    /**
     * 리뷰 단건 조회 (Soft Delete 미포함)
     */
    Optional<Review> findByIdAndIsDeletedFalse(Long id);

    /**
     * 특정 홍보글에 대한 전체 리뷰(삭제 여부 무관)
     */
    List<Review> findByPostId(PromotionPost postId);

    /**
     * 특정 홍보글+사용자 조합 리뷰 조회 (삭제 여부 무관)
     */
    Optional<Review> findByPostIdAndUserId(PromotionPost postId, User userId);

    /**
     * 특정 홍보글+사용자 조합 리뷰 존재여부 (삭제 여부 무관)
     */
    boolean existsByPostIdAndUserId(PromotionPost postId, User userId);
}
