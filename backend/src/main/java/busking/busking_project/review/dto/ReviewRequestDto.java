package busking.busking_project.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 리뷰 생성/수정 요청 DTO
 * - postId: 리뷰 대상 홍보글(PromotionPost) PK
 * - userId: 작성자(유저) PK (실무에서는 인증 정보에서 추출하는 것이 가장 안전함)
 * - comment: 댓글 내용 (필수)
 * - rating: 별점 (1~5, 필수)
 */
@Getter
@Setter
public class ReviewRequestDto {

    @NotNull(message = "리뷰 대상 홍보글 ID는 필수입니다.")
    private Long postId; // 리뷰 대상 PromotionPost PK

    @NotNull(message = "작성자 정보가 누락되었습니다.")
    private Long userId; // 작성자 User PK

    @NotBlank(message = "댓글 내용은 필수 입력값입니다.")
    private String comment;

    @Min(value = 1, message = "별점은 1점 이상이어야 합니다.")
    @Max(value = 5, message = "별점은 5점 이하만 가능합니다.")
    private int rating;
}
