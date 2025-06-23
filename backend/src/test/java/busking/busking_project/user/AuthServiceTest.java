package busking.busking_project.user;

import busking.busking_project.user.dto.RegisterRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService 단위 테스트")
class AuthServiceTest {

    // ── Mock 의존성 ───────────────────────────────────────────────
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private HttpServletRequest request; // Login() 메서드용
    // ✨ 필요 시 OAuth2UserInfoService, TokenRepository 등 추가

    // ── 테스트 대상 ──────────────────────────────────────────────
    @InjectMocks
    private AuthService authService;

    // 공통 fixture
    private User existingUser;

    @BeforeEach
    void setUp() {
        existingUser = User.builder()
                .id(1L)
                .username("user01")
                .password("encodedPw")
                .email("user01@example.com")
                .nickname("닉네임")
                .role(Role.USER)
                .provider(AuthProvider.LOCAL)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /*──────────────────────────────────────────────────────────────
     * register()  ─ “회원가입” 시나리오
     *──────────────────────────────────────────────────────────────*/
    @Nested
    @DisplayName("register(RegisterRequestDto)")
    class Register {

        @Test
        @DisplayName("✅ 중복이 없으면 회원가입이 되고 암호가 인코딩된다")
        void register_success() {
            // Given
            RegisterRequestDto req = new RegisterRequestDto(
                    "user02", "Pa$$1!", "user02@example.com", "닉2");
            when(userRepository.findByUsername("user02")).thenReturn(Optional.empty());
            when(userRepository.findByEmail("user02@example.com")).thenReturn(Optional.empty());
            when(userRepository.findByNickname("닉2")).thenReturn(Optional.empty());
            when(passwordEncoder.encode("Pa$$1!")).thenReturn("encodedPw");
            when(userRepository.save(any(User.class)))
                    .thenAnswer(inv -> {
                        User u = inv.getArgument(0, User.class);
                        u.setId(2L);           // 저장된 PK 세팅
                        return u;
                    });

            // When
            User saved = authService.register(req);

            // Then
            assertThat(saved.getId()).isEqualTo(2L);
            assertThat(saved.getPassword()).isEqualTo("encodedPw");
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("❌ username 이 이미 있으면 예외를 던진다")
        void register_duplicateUsername_fail() {
            // Given
            RegisterRequestDto req = new RegisterRequestDto(
                    "user01", "x", "x@mail.com", "x");

            when(userRepository.findByUsername("user01")).thenReturn(Optional.of(existingUser));

            // When / Then
            assertThatThrownBy(() -> authService.register(req))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("USER_USERNAME_DUPLICATED");
        }
    }

    /*──────────────────────────────────────────────────────────────
     * login(String, String, HttpServletRequest)  ─ “세션 로그인”
     *──────────────────────────────────────────────────────────────*/
    @Nested
    @DisplayName("login(username, password, request")
    class Login {

        @Test
        @DisplayName("✅ 아이디·비밀번호가 맞으면 '로그인 성공' 메시지를 반환한다")
        void login_success() {
            // Given
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(existingUser));
            when(passwordEncoder.matches("plainPw", existingUser.getPassword()))
                    .thenReturn(true);
            when(request.getSession(true)).thenReturn(mock(jakarta.servlet.http.HttpSession.class));

            // When
            String result = authService.login("user01", "plainPw", request);

            // Then
            assertThat(result).contains("로그인 성공");
            verify(request).getSession(true); // 세션이 한번이라도 생성됐는지 확인
        }

        @Test
        @DisplayName("❌ 비밀번호가 틀리면 예외를 던진다")
        void login_badPassword_fail() {
            // Given
            when(userRepository.findByUsername("user01")).thenReturn(Optional.of(existingUser));
            when(passwordEncoder.matches(anyString(), anyString()))
                    .thenReturn(false);

            // When / Then
            assertThatThrownBy(() -> authService.login("user01", "wrong", request))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("USER_NOT_FOUND");
        }
    }

}
