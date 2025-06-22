package busking.busking_project.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByPostId(Long postId);

    Optional<Review> findByPostIdAndUserId(Long postId, Long userId);

    boolean existsByPostIdAndUserId(Long postId, Long userId);

    List<Review> findByPostIdAndIsDeletedFalse(Long postId);

    Optional<Review> findByIdAndIsDeletedFalse(Long id);

    boolean existsByPostIdAndUserIdAndIsDeletedFalse(Long postId, Long userId);

    // ✅ 추가된 별점 평균 계산 쿼리
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.postId = :postId AND r.isDeleted = false")
    Double findAverageRatingByPostId(@Param("postId") Long postId);
}
