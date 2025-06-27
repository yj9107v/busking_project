package busking.busking_project.review;

import busking.busking_project.promotion.PromotionPost;
import busking.busking_project.promotion.PromotionPostRepository;
import busking.busking_project.review.dto.ReviewRequestDto;
import busking.busking_project.review.dto.ReviewResponseDto;
import busking.busking_project.user.User;
import busking.busking_project.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * 리뷰 비즈니스 로직 서비스
 * - PromotionPost, User 객체 참조 기반이며 변수명은 postId로 통일
 */
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PromotionPostRepository promotionPostRepository;

    /**
     * ✅ 게시글별 리뷰 조회 (삭제되지 않은 리뷰만)
     */
    public List<ReviewResponseDto> getReviewsByPost(Long postId) {
        PromotionPost promotionPost = promotionPostRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("홍보글을 찾을 수 없습니다."));
        return reviewRepository.findByPostIdAndIsDeletedFalse(promotionPost).stream()
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
        PromotionPost promotionPost = promotionPostRepository.findById(dto.getPostId())
                .orElseThrow(() -> new NoSuchElementException("홍보글을 찾을 수 없습니다."));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        if (reviewRepository.existsByPostIdAndUserIdAndIsDeletedFalse(promotionPost, user)) {
            throw new IllegalStateException("이미 해당 게시글에 리뷰를 작성하셨습니다.");
        }

        Review review = Review.builder()
                .postId(promotionPost)
                .userId(user)
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

        if (!review.getUserId().getId().equals(userId)) {
            throw new IllegalStateException("리뷰 수정 권한이 없습니다.");
        }

        review.setComment(dto.getComment());
        review.setRating(dto.getRating());
        return new ReviewResponseDto(reviewRepository.save(review));
    }

    /**
     * ✅ 리뷰 삭제 (작성자 본인 또는 관리자만 가능)
     * @param reviewId 삭제할 리뷰 PK
     * @param userId   삭제 요청자 PK
     * @param role     삭제 요청자 역할("USER", "ADMIN" 등)
     */
    @Transactional
    public void deleteReview(Long reviewId, Long userId, String role) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NoSuchElementException("리뷰를 찾을 수 없습니다."));

        // 리뷰 작성자이거나, 관리자면 삭제 허용
        boolean isAuthor = review.getUserId().getId().equals(userId);
        boolean isAdmin = "ADMIN".equalsIgnoreCase(role);

        if (!isAuthor && !isAdmin) {
            throw new IllegalStateException("리뷰 삭제 권한이 없습니다.");
        }

        // Soft Delete 처리
        review.setDeleted(true);
        review.setUpdatedAt(LocalDateTime.now());
        reviewRepository.save(review);
    }

    /**
     * ✅ 사용자 ID 찾기 (로컬 로그인용)
     */
    public Long findUserIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."))
                .getId();
    }
}
