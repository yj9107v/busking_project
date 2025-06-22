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

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Autowired
    private PromotionPostRepository promotionPostRepository;

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

    @Test
    @DisplayName("PromotionPost 수정 서비스 로직")
    void updatePromotionPost() {
        // given
        PromotionPostRequest createRequest = TestDataFactory.defaultPromotionRequestBuilder()
                .title("초기 제목")
                .content("초기 내용")
                .build();

        PromotionPostResponse created = promotionPostService.createPromotionPost(createRequest);
        Long postId = created.getId();

        PromotionPostRequest updateRequest = PromotionPostRequest.builder()
                .title("수정된 제목")
                .content("수정된 내용")
                .category("TALK")
                .place("이대 버스킹존")
                .mediaUrl("https://update.com/img.jpg")
                .build();

        // when
        PromotionPostResponse updated = promotionPostService.updatePromotionPost(postId, updateRequest);

        // then
        assertThat(updated.getId()).isEqualTo(postId);
        assertThat(updated.getTitle()).isEqualTo("수정된 제목");
        assertThat(updated.getContent()).isEqualTo("수정된 내용");
        assertThat(updated.getCategory()).isEqualTo("TALK");
        assertThat(updated.getPlace()).isEqualTo("이대 버스킹존");
        assertThat(updated.getMediaUrl()).contains("update");
    }

    @Test
    @DisplayName("PromotionPost 삭제 서비스 로직 (소프트 삭제)")
    void deletePromotionPost() {
        // given
        PromotionPostRequest request = TestDataFactory.defaultPromotionRequestBuilder()
                .title("삭제 대상")
                .content("삭제될 내용")
                .build();

        PromotionPostResponse created = promotionPostService.createPromotionPost(request);
        Long postId = created.getId();

        // when
        promotionPostService.deletePromotionPost(postId);

        // then
        assertThatThrownBy(() -> promotionPostService.getPostById(postId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("존재하지 않는 게시글");

        // 추가 확인: 실제 DB에서 삭제는 안 되고 isDeleted = true 처리됨
        Optional<PromotionPost> deleted = promotionPostRepository.findById(postId);
        assertThat(deleted).isPresent();
        assertThat(deleted.get().getIsDeleted()).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 ID로 게시글 조회 시 예외 발생")
    void getPostByInvalidId_throwsException() {
        // given
        Long invalidId = 999999L; // 존재하지 않는 ID

        // when & then
        assertThatThrownBy(() -> promotionPostService.getPostById(invalidId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("존재하지 않는 게시글");
    }
}
