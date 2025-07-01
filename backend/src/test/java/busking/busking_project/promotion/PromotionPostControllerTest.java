package busking.busking_project.promotion;

import busking.busking_project.common.TestDataFactory;
import busking.busking_project.promotion.dto.PromotionPostRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PromotionPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("POST /api/promotions - 홍보 게시글 생성 성공")
    void createPromotionPost_success() throws Exception {
        // given
        PromotionPostRequest request = TestDataFactory.defaultPromotionRequestBuilder().build();
        String json = objectMapper.writeValueAsString(request);

        // when & then
        mockMvc.perform(post("/api/promotions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    @DisplayName("유효하지 않은 제목으로 게시글 생성 시 400 반환")
    void createPromotionPost_invalidTitle_returnsBadRequest() throws Exception {
        // given
        PromotionPostRequest request = PromotionPostRequest.builder()
                .title("") // ✅ 공백 → 실패 조건
                .content("내용")
                .category("MUSIC")
                .place("홍대")
                .mediaUrl(null)
                .build();

        // when & then
        mockMvc.perform(post("/api/promotions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("허용되지 않은 카테고리로 게시글 생성 시 400 반환")
    void createPromotionPost_invalidCategory_returnsBadRequest() throws Exception {
        // given
        PromotionPostRequest request = PromotionPostRequest.builder()
                .title("제목")
                .content("내용")
                .category("INVALID") // ✅ 존재하지 않는 enum
                .place("홍대")
                .mediaUrl(null)
                .build();

        // when & then
        mockMvc.perform(post("/api/promotions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}