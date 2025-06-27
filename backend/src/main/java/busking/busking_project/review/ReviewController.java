package busking.busking_project.review;

import busking.busking_project.review.dto.ReviewResponseDto;
import busking.busking_project.user.User;
import busking.busking_project.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 리뷰 관련 REST API 컨트롤러
 */
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final UserRepository userRepository;

    /**
     * 특정 홍보글(postId)에 대한 모든 리뷰 조회
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByPost(@PathVariable Long postId) {
        List<ReviewResponseDto> reviews = reviewService.getReviewsByPost(postId);
        return ResponseEntity.ok(reviews);
    }

    /**
     * 리뷰 단건 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> getReviewById(@PathVariable Long id) {
        ReviewResponseDto review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    /**
     * 리뷰 삭제
     * - 인증 정보(Authentication)에서 userId, role을 추출하여 본인/관리자만 삭제 가능
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id, Authentication auth) {
        try {
            Long userId = extractUserId(auth);
            String role = extractUserRole(auth);
            reviewService.deleteReview(id, userId, role);
            return ResponseEntity.ok("리뷰가 성공적으로 삭제되었습니다.");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return ResponseEntity.status(403).body("리뷰 삭제 권한이 없습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("리뷰 삭제 중 오류가 발생했습니다.");
        }
    }

    /**
     * 인증(Authentication) 객체에서 실제 사용자 userId 추출
     * - OAuth2 로그인/로컬 로그인 모두 지원
     */
    private Long extractUserId(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("로그인된 사용자가 아닙니다.");
        }

        // OAuth2 로그인 사용자 (소셜)
        if (auth.getPrincipal() instanceof DefaultOAuth2User oAuth2User) {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            Object userId = attributes.get("userId");
            if (userId != null) {
                return Long.parseLong(userId.toString());
            } else {
                throw new IllegalStateException("OAuth2 인증 사용자 ID를 찾을 수 없습니다.");
            }
        }

        // 로컬 로그인 사용자
        if (auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User user) {
            String username = user.getUsername();
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."))
                    .getId();
        }

        throw new IllegalStateException("알 수 없는 인증 유형입니다.");
    }

    /**
     * 인증(Authentication) 객체에서 실제 사용자 role(권한) 추출
     * - OAuth2/로컬 모두 대응 ("ADMIN", "USER" 등 반환)
     */
    private String extractUserRole(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("로그인된 사용자가 아닙니다.");
        }

        // Spring Security의 권한 구조상 첫 번째 권한만 사용 (여러 개일 경우 첫 번째)
        return auth.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", "")) // "ROLE_ADMIN" → "ADMIN"
                .orElse("USER");
    }
}
