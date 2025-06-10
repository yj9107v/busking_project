
package busking.busking_project.user;

import busking.busking_project.user.dto.RegisterRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 회원가입_후_로그인_성공() throws Exception {
        // ✅ 유효성 조건을 충족하는 회원가입 요청
        RegisterRequestDto request = new RegisterRequestDto();
        request.setUsername("valid_user01");  // 영문+숫자+밑줄, 5~20자
        request.setPassword("Valid123!");     // 영문+숫자+특수문자, 8~20자
        request.setEmail("valid@example.com");
        request.setNickname("테스터1");         // 한글+숫자, 2~30자

        // 회원가입 요청
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());

        // 로그인 요청
        String loginJson = """
            {
                "username": "valid_user01",
                "password": "Valid123!"
            }
        """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists())
                .andExpect(jsonPath("$.data.user.username").value("valid_user01"));
    }
}
