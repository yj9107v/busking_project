package busking.busking_project.promotion;

import busking.busking_project.common.TestDataFactory;
import busking.busking_project.promotion.dto.PromotionPostRequest;
import busking.busking_project.promotion.dto.PromotionPostResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 🧪 Service 레이어 단위 테스트
 * <p>
 * ▸ 게시글 생성 비즈니스 로직이 정상 수행되는지 검증한다.
 * </p>
 */
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class PromotionPostServiceTest {

    @Autowired
    private PromotionPostService promotionPostService;

    /**
     * <pre>
     * given  : PromotionPostRequest DTO 생성(TestDataFactory 사용)
     * when   : service.createPromotionPost 호출
     * then   : 반환 DTO 필드 값이 입력값과 일치한다
     * </pre>
     */
    @Test
    @DisplayName("PromotionPost 생성 서비스 로직")
    void createPromotionPost() {
        // given
        PromotionPostRequest request = TestDataFactory.defaultPromotionRequestBuilder()
                .title("서비스 테스트 제목")
                .content("서비스 테스트 내용")
                .build();

        // when
        PromotionPostResponse response = promotionPostService.createPromotionPost(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("서비스 테스트 제목");
        assertThat(response.getCategory()).isEqualTo("MUSIC");
        assertThat(response.getPlace()).isEqualTo("홍대 버스킹존");
    }
}
