package busking.busking_project.promotion;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PromotionPostCreateTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ 정상적으로 게시글을 등록할 수 있는 경우
    @Test
    @DisplayName("홍보 게시글 등록 성공 테스트")
    void testCreatePromotionPost_Success() throws Exception {
        PromotionPostRequest request = new PromotionPostRequest();
        request.setTitle("버스킹 공연 알림");
        request.setContent("홍대 앞에서 공연합니다!");
        request.setCategory("MUSIC");
        request.setMediaUrl("https://example.com/test.jpg");
        request.setPlace("홍대 걷고싶은 거리");

        mockMvc.perform(post("/api/promotions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()); // 201 Created
    }

    // ❌ 제목 누락 시 실패
    @Test
    @DisplayName("제목이 없으면 게시글 생성 실패")
    void testCreatePromotionPost_NoTitle_ShouldFail() throws Exception {
        PromotionPostRequest request = new PromotionPostRequest();
        request.setTitle(""); // 빈 제목
        request.setContent("내용 있음");
        request.setCategory("MUSIC");
        request.setMediaUrl(null);
        request.setPlace("홍대");

        mockMvc.perform(post("/api/promotions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // 400 Bad Request
    }

    // ❌ 내용 누락 시 실패
    @Test
    @DisplayName("내용이 없으면 게시글 생성 실패")
    void testCreatePromotionPost_NoContent_ShouldFail() throws Exception {
        PromotionPostRequest request = new PromotionPostRequest();
        request.setTitle("제목 있음");
        request.setContent(""); // 빈 내용
        request.setCategory("MUSIC");
        request.setMediaUrl(null);
        request.setPlace("홍대");

        mockMvc.perform(post("/api/promotions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ❌ 잘못된 카테고리 입력 시 실패
    @Test
    @DisplayName("잘못된 카테고리 입력 시 게시글 생성 실패")
    void testCreatePromotionPost_InvalidCategory_ShouldFail() throws Exception {
        PromotionPostRequest request = new PromotionPostRequest();
        request.setTitle("제목 있음");
        request.setContent("내용 있음");
        request.setCategory("FOOD"); // 잘못된 카테고리
        request.setMediaUrl(null);
        request.setPlace("홍대");

        mockMvc.perform(post("/api/promotions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ❌ 장소 누락 시 실패
    @Test
    @DisplayName("장소가 없으면 게시글 생성 실패")
    void testCreatePromotionPost_NoPlace_ShouldFail() throws Exception {
        PromotionPostRequest request = new PromotionPostRequest();
        request.setTitle("제목 있음");
        request.setContent("내용 있음");
        request.setCategory("MUSIC");
        request.setMediaUrl(null);
        request.setPlace(""); // 빈 장소

        mockMvc.perform(post("/api/promotions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
