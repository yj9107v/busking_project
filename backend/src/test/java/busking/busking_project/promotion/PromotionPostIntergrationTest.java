package busking.busking_project.promotion;

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

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "testuser", roles = "USER")
public class PromotionPostIntergrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @DisplayName("홍보 게시글 통합 테스트 - 생성, 조회, 수정, 삭제")
    void promotionPostIntegrationTest() throws Exception {
        // 1. Create
        PromotionPostRequest createRequest = new PromotionPostRequest();
        createRequest.setTitle("테스트 게시글");
        createRequest.setCategory("MUSIC");
        createRequest.setPlace("서울 홍대입구");
        createRequest.setContent("테스트 내용입니다.");
        createRequest.setMediaUrl("http://test.com/image.jpg");

        String createJson = objectMapper.writeValueAsString(createRequest);

        String location = mockMvc.perform(post("/api/promotions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        assert location != null;
        String[] segments = location.split("/");
        Long postId = Long.parseLong(segments[segments.length - 1]);

        // 2. Read
        mockMvc.perform(get("/api/promotions/" + postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("테스트 게시글")));

        // 3. Update
        PromotionPostRequest updateRequest = new PromotionPostRequest();
        updateRequest.setTitle("수정된 게시글");
        updateRequest.setCategory("ART");
        updateRequest.setPlace("서울 강남역");
        updateRequest.setContent("수정된 내용입니다.");
        updateRequest.setMediaUrl("http://test.com/updated.jpg");

        mockMvc.perform(put("/api/promotions/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정된 게시글"));

        // 4. Delete
        mockMvc.perform(delete("/api/promotions/" + postId))
                .andExpect(status().isNoContent());

        // 5. Read after delete
        mockMvc.perform(get("/api/promotions/" + postId))
                .andExpect(status().isNotFound());
    }
}