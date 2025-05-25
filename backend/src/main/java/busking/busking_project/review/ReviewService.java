package busking.busking_project.review;

import busking.busking_project.review.dto.ReviewRequestDto;
import busking.busking_project.review.dto.ReviewResponseDto;
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
    private final busking.busking_project.user.UserRepository UserRepository;

    /**
     * ✅ 게시글별 리뷰 조회 (삭제되지 않은 리뷰만)
     */
    public List<ReviewResponseDto> getReviewsByPost(Long postId) {
        return reviewRepository.findByPostIdAndIsDeletedFalse(postId).stream()
                .map(ReviewResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * ✅ 단일 리뷰 조회 (삭제되지 않은 리뷰만)
     */
    public ReviewResponseDto getReviewById(Long id) {
        return reviewRepository.findByIdAndIsDeletedFalse(id)
                .map(ReviewResponseDto::new)
                .orElseThrow(() -> new NoSuchElementException("리뷰를 찾을 수 없습니다."));
    }

    /**
     * ✅ 리뷰 생성
     */
    @Transactional
    public ReviewResponseDto createReview(ReviewRequestDto dto) {
        // 중복 리뷰 검사
        if (reviewRepository.existsByPostIdAndUserIdAndIsDeletedFalse(dto.getPostId(), dto.getUserId())) {
            throw new IllegalStateException("이미 해당 게시글에 리뷰를 작성하셨습니다.");
        }

        // 리뷰 생성
        Review review = Review.builder()
                .postId(dto.getPostId())
                .userId(dto.getUserId())
                .comment(dto.getComment())
                .rating(dto.getRating())
                .isDeleted(false)
                .build();

        Review savedReview = reviewRepository.save(review);
        return new ReviewResponseDto(savedReview);
    }

    /**
     * ✅ 리뷰 수정 (작성자 본인만 가능)
     */
    @Transactional
    public ReviewResponseDto updateReview(Long reviewId, Long userId, ReviewRequestDto dto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NoSuchElementException("리뷰를 찾을 수 없습니다."));

        if (!review.getUserId().equals(userId)) {
            throw new IllegalStateException("리뷰 수정 권한이 없습니다.");
        }

        review.update(dto.getComment(), dto.getRating());
        return new ReviewResponseDto(reviewRepository.save(review));
    }

    /**
     * ✅ 리뷰 삭제 (작성자 본인만 가능)
     */
    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NoSuchElementException("리뷰를 찾을 수 없습니다."));

        if (!review.getUserId().equals(userId)) {
            throw new IllegalStateException("리뷰 삭제 권한이 없습니다.");
        }

        // 실제 DB에서 삭제
        reviewRepository.delete(review);
    }

    /**
     * ✅ 사용자 ID 찾기 (로컬 로그인용)
     */
    public Long findUserIdByUsername(String username) {
        return UserRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."))
                .getId();
    }
}
