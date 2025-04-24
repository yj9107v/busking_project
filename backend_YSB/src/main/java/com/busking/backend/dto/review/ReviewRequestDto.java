package com.busking.backend.dto.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto {
    private Long postId;
    private Long userId;
    private int rating;
    private String comment;
}
