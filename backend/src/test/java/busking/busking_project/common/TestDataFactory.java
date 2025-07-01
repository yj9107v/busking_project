package busking.busking_project.common;

import busking.busking_project.promotion.PromotionPost;
import busking.busking_project.promotion.dto.PromotionPostRequest;
import lombok.experimental.UtilityClass;

/**
 * 🚀 공통 테스트 데이터 생성 헬퍼
 * <p>
 * ▸ PromotionPostRequest·PromotionPost 엔티티 빌더 메서드를 통합해 테스트 중복을 제거한다.<br>
 * ▸ 필요 시 User, Location 등 다른 도메인 객체 생성 메서드도 이곳에 확장한다.
 * </p>
 */
@UtilityClass
public class TestDataFactory {

    /**
     * 기본값이 채워진 PromotionPostRequest 빌더를 반환한다.
     * <p>필요한 필드만 덮어써서 사용한다.</p>
     */
    public static PromotionPostRequest.PromotionPostRequestBuilder defaultPromotionRequestBuilder() {
        return PromotionPostRequest.builder()
                .title("기본 제목")
                .content("기본 내용")
                .category("MUSIC")
                .place("홍대 버스킹존")
                .mediaUrl("https://example.com/image.jpg"); // optional
    }

    /**
     * PromotionPost 엔티티 생성 편의 메서드.
     *
     * @param title   게시글 제목
     * @param content 게시글 내용
     * @param userId  작성자 ID
     * @return 생성된 PromotionPost 엔티티
     */
    public PromotionPost promotionPostEntity(String title, String content, Long userId) {
        return PromotionPost.builder()
                .title(title)
                .content(content)
                .userId(userId)
                .build();
    }
}
