
package busking.busking_project.user;

import busking.busking_project.IntegrationTestSupport;
import busking.busking_project.user.dto.RegisterRequestDto;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthControllerTest extends IntegrationTestSupport {

    /*───────────────────────────────────────────────────────────────*
     *  1) 회원가입 관련
     *───────────────────────────────────────────────────────────────*/
    @Nested
    @DisplayName("회원가입 API (/api/users/register)")
    class Register {

        @Test
        @DisplayName("✅ 정상 회원가입")
        void register_success() throws Exception {
            // ✅ 유효성 조건을 충족하는 회원가입 요청
            RegisterRequestDto req = RegisterRequestDto.builder()
                    .username("dupUser")
                    .password("Pass123!")
                    .email("dup2@example.com")
                    .nickname("닉2")
                    .build();

            // 회원가입 요청
            mockMvc.perform(post("/api/users/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(req)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.username").value("dupUser"));
        }

        @Test
        @DisplayName("❌ 아이디 중복 → 400")
        void register_usernameDuplicated() throws Exception {
            // 1) 선가입
            register("dupUser", "Pass123!", "dup1@example.com", "닉1");

            // 2) 같은 username 재요청
            RegisterRequestDto dup = RegisterRequestDto.builder()
                    .username("dupUser")
                    .password("Pass123!")
                    .email("dup1@example.com")
                    .nickname("닉1")
                    .build();

            mockMvc.perform(post("/api/users/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dup)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value("USER_USERNAME_DUPLICATED"));
        }
    }

    /*───────────────────────────────────────────────────────────────*
     *  3) 인증 없이 개인정보 요청 → 401
     *───────────────────────────────────────────────────────────────*/
    @Test
    @DisplayName("❌ 미인증 사용자가 /users/me 호출 → 401")
    void me_unauthenticated() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    /*───────────────────────────────────────────────────────────────*
     *  2) 로그인 & 세션
     *───────────────────────────────────────────────────────────────*/
    @Nested
    @DisplayName("로그인 API (/api/auth/login)")
    class Login {

        @Test
        @DisplayName("✅ 로그인 성공 후 세션‧토큰 획득")
        void login_success() throws Exception {
            // --- 준비: 회원가입 ---
            register("loginUser", "Pass123!", "login@example.com", "로그인유저");

            // --- 로그인 ---
            MvcResult result = mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            { "username": "loginUser", "password": "Pass123!" }
                            """))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.accessToken").exists())
                    .andReturn();

            // 응답 JSON 파싱
            JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
            String accessToken = root.at("/data/accessToken").asText();

            // 세션 객체
            MockHttpSession session = (MockHttpSession)  result.getRequest().getSession(false);
            assertThat(session).isNotNull();

            // --- 세션 & 토큰을 활용해 내 정보 조회 ---
            mockMvc.perform(get("/api/users/me")
                            .session(session)
                            .header("Authorization", "Bearer " + accessToken))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value("loginUser"))
                    .andExpect(jsonPath("$.nickname").value("로그인유저"));
        }

        @Test
        @DisplayName("❌ 비밀번호 오류 → 401")
        void login_wrongPassword() throws Exception {
            register("wrongPw", "Pass123!", "wrongpw@example.com", "닉네임");

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    { "username": "wrongPw", "password": "Wrong!!" }
                                    """))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.errorCode").value("USER_NOT_FOUND"));
        }
    }


    /*───────────────────────────────────────────────────────────────*
     *  📌 테스트 헬퍼: 회원가입 공통 메서드
     *───────────────────────────────────────────────────────────────*/
    private void register(String username, String password,
                          String email, String nickname) throws Exception {

        RegisterRequestDto req = RegisterRequestDto.builder()
                .username(username)
                .password(password)
                .email(email)
                .nickname(nickname)
                .build();

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
