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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PromotionPostUpdateTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("홍보 게시글 수정 성공 테스트")
    void testUpdatePromotionPost_Success() throws Exception {
        // 게시글 생성
        PromotionPostRequest createRequest = new PromotionPostRequest();
        createRequest.setTitle("수정 전 제목");
        createRequest.setContent("수정 전 내용");
        createRequest.setCategory("MUSIC");
        createRequest.setMediaUrl("https://example.com/original.jpg");
        createRequest.setPlace("홍대");

        String response = mockMvc.perform(post("/api/promotions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long postId = objectMapper.readTree(response).get("id").asLong();

        // 수정 요청
        PromotionPostRequest updateRequest = new PromotionPostRequest();
        updateRequest.setTitle("수정된 제목");
        updateRequest.setContent("수정된 내용");
        updateRequest.setCategory("MUSIC");
        updateRequest.setMediaUrl("https://example.com/updated.jpg");
        updateRequest.setPlace("강남");

        mockMvc.perform(put("/api/promotions/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }
}
