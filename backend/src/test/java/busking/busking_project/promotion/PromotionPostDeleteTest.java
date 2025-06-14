package busking.busking_project.promotion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import busking.busking_project.promotion.dto.PromotionPostRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PromotionPostDeleteTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("홍보 게시글 삭제 성공 테스트")
    void testDeletePromotionPost_Success() throws Exception {
        // 1. 게시글 먼저 생성
        PromotionPostRequest createRequest = new PromotionPostRequest();
        createRequest.setTitle("삭제 테스트 제목");
        createRequest.setContent("삭제 테스트 내용");
        createRequest.setCategory("MUSIC");
        createRequest.setMediaUrl("https://example.com/delete.jpg");
        createRequest.setPlace("홍대");

        String response = mockMvc.perform(post("/api/promotions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        Long postId = jsonNode.get("id").asLong();

        // 2. 삭제 요청
        mockMvc.perform(delete("/api/promotions/" + postId))
                .andExpect(status().isNoContent()); // 204 No Content
    }

    @Test
    @DisplayName("존재하지 않는 게시글 삭제 시 404 응답")
    void testDeletePromotionPost_NotFound() throws Exception {
        Long nonExistentId = 999999L;

        mockMvc.perform(delete("/api/promotions/" + nonExistentId))
                .andExpect(status().isNotFound()); // 404 Not Found
    }
}
