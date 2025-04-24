package com.busking.backend.dto.review;

import com.busking.backend.domain.promotion.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponseDto {

    private final Long id;
    private final Long postId;
    private final Long userId;
    private final int rating;
    private final String comment;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.postId = review.getPostId();
        this.userId = review.getUserId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }
}