package busking.busking_project.review.dto;

import busking.busking_project.review.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewResponseDto {

    private Long id;
    private Long postId;
    private Long userId;
    private String comment;
    private int rating;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.postId = review.getPostId();
        this.userId = review.getUserId();
        this.comment = review.getComment();
        this.rating = review.getRating();
        this.isDeleted = review.isDeleted();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }
}
