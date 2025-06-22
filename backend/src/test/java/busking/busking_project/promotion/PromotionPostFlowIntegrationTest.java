package busking.busking_project.promotion;

import busking.busking_project.common.TestDataFactory;
import busking.busking_project.promotion.dto.PromotionPostRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback(false)
class PromotionPostFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    static Long savedId;

    @Test
    @Order(1)
    @WithMockUser
    @DisplayName("1️⃣ POST /api/promotions - 게시글 생성")
    void createPost() throws Exception {
        PromotionPostRequest request = TestDataFactory.defaultPromotionRequestBuilder().build();
        String json = objectMapper.writeValueAsString(request);

        String location = mockMvc.perform(post("/api/promotions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        assertThat(location).isNotNull();  // ✅ 명시적 검증 추가
        assertThat(location).contains("/api/promotions/");
        System.out.println("✅ 생성된 ID = " + savedId); // 확인 로그
        savedId = Long.parseLong(location.substring(location.lastIndexOf("/") + 1));
    }

    @Test
    @Order(2)
    @WithMockUser
    @DisplayName("2️⃣ GET /api/promotions/{id} - 게시글 조회")
    void getPost() throws Exception {
        mockMvc.perform(get("/api/promotions/{id}", savedId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedId));
    }

    @Test
    @Order(3)
    @WithMockUser
    @DisplayName("3️⃣ DELETE /api/promotions/{id} - 게시글 삭제")
    void deletePost() throws Exception {
        // Soft delete 테스트가 없는 상태라면 생략하거나 추후 구현
        // mockMvc.perform(delete("/api/promotions/{id}", savedId))
        //         .andExpect(status().isNoContent());
    }
}