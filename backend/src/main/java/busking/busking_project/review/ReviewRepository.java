package busking.busking_project.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByPostId(Long postId);
    Optional<Review> findByPostIdAndUserId(Long postId, Long userId);
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    List<Review> findByPostIdAndIsDeletedFalse(Long postId);
    Optional<Review> findByIdAndIsDeletedFalse(Long id);
    boolean existsByPostIdAndUserIdAndIsDeletedFalse(Long postId, Long userId);
}
