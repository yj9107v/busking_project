package com.busking.backend.repository;

import com.busking.backend.domain.promotion.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 게시글 기준 리뷰 전체 조회
    List<Review> findByPostIdAndIsDeletedFalse(Long postId);

    // 사용자 1명이 리뷰를 1개만 작성할 수 있게 체크
    Optional<Review> findByPostIdAndUserIdAndIsDeletedFalse(Long postId, Long userId);
}