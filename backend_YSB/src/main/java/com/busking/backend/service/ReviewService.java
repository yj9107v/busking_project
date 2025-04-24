package com.busking.backend.service;

import com.busking.backend.domain.promotion.Review;
import com.busking.backend.dto.review.ReviewRequestDto;
import com.busking.backend.dto.review.ReviewResponseDto;
import com.busking.backend.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    // 리뷰 등록
    @Transactional
    public ReviewResponseDto createReview(ReviewRequestDto dto) {
        Review review = Review.builder()
                .postId(dto.getPostId())
                .userId(dto.getUserId())
                .rating(dto.getRating())
                .comment(dto.getComment())
                .isDeleted(false)
                .build();

        return new ReviewResponseDto(reviewRepository.save(review));
    }

    // 리뷰 목록 조회
    public List<ReviewResponseDto> getReviewsByPost(Long postId) {
        return reviewRepository.findByPostIdAndIsDeletedFalse(postId).stream()
                .map(ReviewResponseDto::new)
                .collect(Collectors.toList());
    }

    // 리뷰 수정
    @Transactional
    public ReviewResponseDto updateReview(Long id, ReviewRequestDto dto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("리뷰를 찾을 수 없습니다."));

        review.update(dto.getRating(), dto.getComment());
        return new ReviewResponseDto(reviewRepository.save(review));
    }

    // 리뷰 삭제 (Soft Delete)
    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("리뷰를 찾을 수 없습니다."));

        review.setIsDeleted(true);
        reviewRepository.save(review);
    }
}