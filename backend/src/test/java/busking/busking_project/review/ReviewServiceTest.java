package busking.busking_project.review;

import busking.busking_project.review.dto.ReviewRequestDto;
import busking.busking_project.review.dto.ReviewResponseDto;
import busking.busking_project.review.Review;
import busking.busking_project.review.ReviewService;
import busking.busking_project.review.ReviewRepository;
import busking.busking_project.user.Role;
import busking.busking_project.user.User;
import busking.busking_project.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ReviewServiceTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        reviewService = new ReviewService(reviewRepository, userRepository);
    }

    @Test
    @DisplayName("리뷰 등록 성공")
    void createReview_success() {
        // given
        User user = User.builder()
                .username("testuser")
                .password("encodedpw")
                .nickname("nick")
                .role(Role.USER)
                .build();
        userRepository.save(user);

        ReviewRequestDto request = ReviewRequestDto.builder()
                .postId(1L)
                .userId(user.getId())
                .comment("훌륭한 공연이었어요!")
                .rating(5)
                .build();

        // when
        reviewService.createReview(request);

        // then
        List<Review> all = reviewRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getComment()).isEqualTo("훌륭한 공연이었어요!");
    }

    // 이후 getReviewById, getReviewsByPost, updateReview, deleteReview 등의 테스트 추가 예정
}
