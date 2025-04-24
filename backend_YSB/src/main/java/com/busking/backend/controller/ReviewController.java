package com.busking.backend.controller;

import com.busking.backend.dto.review.ReviewRequestDto;
import com.busking.backend.dto.review.ReviewResponseDto;
import com.busking.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // ✅ 리뷰 등록
    @PostMapping
    public ResponseEntity<ReviewResponseDto> create(@RequestBody ReviewRequestDto dto) {
        return ResponseEntity.ok(reviewService.createReview(dto));
    }

    // ✅ 게시글 리뷰 목록
    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> getByPost(@RequestParam Long postId) {
        return ResponseEntity.ok(reviewService.getReviewsByPost(postId));
    }

    // ✅ 리뷰 수정
    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> update(@PathVariable Long id, @RequestBody ReviewRequestDto dto) {
        return ResponseEntity.ok(reviewService.updateReview(id, dto));
    }

    // ✅ 리뷰 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}