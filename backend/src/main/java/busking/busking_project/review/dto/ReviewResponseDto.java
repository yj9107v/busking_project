package busking.busking_project.review.dto;

import busking.busking_project.review.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 리뷰 조회 응답 DTO
 * - Review 엔티티의 주요 정보만 추출하여 프론트로 전달
 */
@Getter
@Setter
public class ReviewResponseDto {

    private Long id;              // 리뷰 PK
    private Long postId;          // 홍보글 PK (PromotionPost)
    private Long userId;          // 작성자(유저) PK
    private String comment;       // 댓글 내용
    private int rating;           // 별점
    private boolean isDeleted;    // 삭제 여부
    private LocalDateTime createdAt; // 생성일시
    private LocalDateTime updatedAt; // 수정일시

    /**
     * Review 엔티티 → DTO 변환 생성자
     */
    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.postId = review.getPostId().getId(); // PromotionPost 객체 참조에서 PK 추출
        this.userId = review.getUserId().getId();          // User 객체 참조에서 PK 추출
        this.comment = review.getComment();
        this.rating = review.getRating();
        this.isDeleted = review.isDeleted();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }
}
