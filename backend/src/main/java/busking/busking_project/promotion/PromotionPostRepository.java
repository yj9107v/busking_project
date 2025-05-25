package busking.busking_project.promotion;

import busking.busking_project.promotion.PromotionPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromotionPostRepository extends JpaRepository<PromotionPost, Long> {

    // UUID 기반 단건 조회
    Optional<PromotionPost> findByUuid(String uuid);

    // soft delete 안 된 게시글만 조회
    Optional<PromotionPost> findByIdAndIsDeletedFalse(Long id);

    // 삭제되지 않은 전체 목록
    java.util.List<PromotionPost> findAllByIsDeletedFalse();
}
