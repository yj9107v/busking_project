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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PromotionPostReadTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("전체 홍보 게시글 목록 조회 테스트")
    void testGetAllPromotionPosts() throws Exception {
        mockMvc.perform(get("/api/promotions"))
                .andExpect(status().isOk()); // 200 OK
    }

    @Test
    @DisplayName("단일 홍보 게시글 조회 테스트")
    void testGetPromotionPostById() throws Exception {
        // 게시글 생성
        PromotionPostRequest request = new PromotionPostRequest();
        request.setTitle("조회 테스트 제목");
        request.setContent("조회용 내용입니다.");
        request.setCategory("MUSIC");
        request.setMediaUrl("https://example.com/view.jpg");
        request.setPlace("홍대");

        String response = mockMvc.perform(post("/api/promotions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        // ID로 단건 조회
        mockMvc.perform(get("/api/promotions/" + id))
                .andExpect(status().isOk()); // 200 OK
    }

    @Test
    @DisplayName("존재하지 않는 ID 조회 시 404 반환")
    void testGetPromotionPostById_NotFound() throws Exception {
        mockMvc.perform(get("/api/promotions/9999999"))
                .andExpect(status().isNotFound()); // 404 Not Found
    }
}
