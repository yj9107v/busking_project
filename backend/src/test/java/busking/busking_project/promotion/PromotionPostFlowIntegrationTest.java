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

        assertThat(location).isNotNull();
        assertThat(location).contains("/api/promotions/");
        savedId = Long.parseLong(location.substring(location.lastIndexOf("/") + 1));
        System.out.println("✅ 생성된 ID = " + savedId);
    }

    @Test
    @Order(2)
    @WithMockUser
    @DisplayName("2️⃣ PUT /api/promotions/{id} - 게시글 수정")
    void updatePost() throws Exception {
        PromotionPostRequest updateRequest = PromotionPostRequest.builder()
                .title("수정된 제목입니다")
                .content("수정된 내용입니다")
                .category("TALK")
                .place("홍대")
                .mediaUrl("https://test.com/image.jpg")
                .build();

        String json = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(put("/api/promotions/{id}", savedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정된 제목입니다"));
    }

    @Test
    @Order(3)
    @WithMockUser
    @DisplayName("3️⃣ GET /api/promotions/{id} - 수정된 게시글 조회")
    void getUpdatedPost() throws Exception {
        mockMvc.perform(get("/api/promotions/{id}", savedId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정된 제목입니다"));
    }

    @Test
    @Order(4)
    @WithMockUser
    @DisplayName("4️⃣ DELETE /api/promotions/{id} - 게시글 삭제")
    void deletePost() throws Exception {
        mockMvc.perform(delete("/api/promotions/{id}", savedId))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(5)
    @WithMockUser
    @DisplayName("5️⃣ GET /api/promotions/{id} - 삭제된 게시글 조회 실패")
    void getDeletedPost_shouldFail() throws Exception {
        mockMvc.perform(get("/api/promotions/{id}", savedId))
                .andExpect(status().isNotFound());
    }
}