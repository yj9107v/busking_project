package busking.busking_project.promotion;

import busking.busking_project.common.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class PromotionPostRepositoryTest {

    @Autowired
    private PromotionPostRepository promotionPostRepository;

    @Test
    @DisplayName("PromotionPost 저장 후 ID로 조회")
    void saveAndFindById() {
        // given
        PromotionPost post = PromotionPost.builder()
                .uuid("test-uuid-1234")
                .userId(1L)
                .title("테스트 제목")
                .content("테스트 내용")
                .category(PromotionPost.Category.MUSIC)
                .place("신촌 거리")
                .build();

        // when
        PromotionPost saved = promotionPostRepository.save(post);
        Optional<PromotionPost> found = promotionPostRepository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("테스트 제목");
    }
}