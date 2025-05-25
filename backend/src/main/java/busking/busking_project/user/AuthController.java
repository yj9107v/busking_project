package busking.busking_project.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController // ✅ REST API 컨트롤러로 동작 (JSON 반환)
@RequestMapping("/api/users") // ✅ 기본 URL 경로: /api/users
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // ✅ CORS 허용 (React 연동)
public class AuthController {

    @Autowired private AuthService authService;
    @Autowired private UserRepository userRepository;

    /**
     * ✅ 회원가입 처리
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request, BindingResult bindingResult) {
        // 🔍 유효성 검사 실패 시 에러 응답 반환
        if (bindingResult.hasErrors()) {
            String field = bindingResult.getFieldError().getField();
            String message = bindingResult.getFieldError().getDefaultMessage();

            // 🔄 필드에 따라 에러 코드 매핑
            String errorCode = switch (field) {
                case "email" -> "USER_EMAIL_INVALID_FORMAT";
                case "username" -> "USER_USERNAME_INVALID_FORMAT";
                case "password" -> "USER_PASSWORD_INVALID_FORMAT";
                default -> "VALIDATION_FAILED";
            };

            // ⚠️ 에러 응답 구성
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", message);
            errorResponse.put("errorCode", errorCode);
            errorResponse.put("data", null);

            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            // ✅ 회원 가입 처리
            User savedUser = authService.register(request);

            // 🔄 사용자 데이터 구성
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", savedUser.getId());
            userData.put("username", savedUser.getUsername());
            userData.put("nickname", savedUser.getNickname());
            userData.put("created_at", savedUser.getCreatedAt());

            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("success", true);
            successResponse.put("message", "회원가입이 성공적으로 완료되었습니다.");
            successResponse.put("data", userData);

            return ResponseEntity.ok(successResponse);

        } catch (CustomException e) {
            // ⚠️ 커스텀 예외 처리 (닉네임 중복 등)
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("errorCode", e.getErrorCode());
            errorResponse.put("data", null);
            return ResponseEntity.badRequest().body(errorResponse);

        } catch (RuntimeException e) {
            // ⚠️ 기본 런타임 예외 처리 (ID/Email 중복 등)
            String errorCode = e.getMessage();
            String message;

            switch (errorCode) {
                case "USER_USERNAME_DUPLICATED" -> message = "이미 사용 중인 아이디입니다.";
                case "USER_EMAIL_DUPLICATED" -> message = "이미 사용 중인 이메일입니다.";
                case "USER_NICKNAME_DUPLICATED" -> message = "이미 사용 중인 닉네임입니다.";
                default -> {
                    errorCode = "UNKNOWN_ERROR";
                    message = Objects.requireNonNullElse(e.getMessage(), "알 수 없는 오류가 발생했습니다.");
                }
            }

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", message);
            errorResponse.put("errorCode", errorCode);
            errorResponse.put("data", null);

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * ✅ 로그인 처리 (세션 + 토큰)
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> user, HttpServletRequest request) {
        try {
            // 🔑 로그인 후 토큰 생성
            LoginResponse loginResponse = authService.loginWithTokens(
                    user.get("username"),
                    user.get("password"),
                    request
            );

            // 🔄 사용자 정보 구성
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", loginResponse.getUser().getId());
            userData.put("username", loginResponse.getUser().getUsername());
            userData.put("nickname", loginResponse.getUser().getNickname());
            userData.put("email", loginResponse.getUser().getEmail());
            userData.put("provider", "local");

            // 🔄 토큰 정보 구성
            Map<String, Object> tokenData = new HashMap<>();
            tokenData.put("accessToken", loginResponse.getAccessToken());
            tokenData.put("refreshToken", loginResponse.getRefreshToken());
            tokenData.put("user", userData);

            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("success", true);
            successResponse.put("message", "로그인이 성공적으로 완료되었습니다.");
            successResponse.put("data", tokenData);

            return ResponseEntity.ok(successResponse);

        } catch (RuntimeException e) {
            // ❌ 로그인 실패 처리
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "아이디 또는 비밀번호가 잘못되었습니다.");
            errorResponse.put("errorCode", "USER_NOT_FOUND");
            errorResponse.put("data", null);
            return ResponseEntity.status(401).body(errorResponse);
        }
    }

    /**
     * ✅ 소셜 로그인 성공 시 프론트로 리디렉션
     */
    @GetMapping("/login-success")
    public void loginSuccess(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:3000");
    }

    /**
     * ✅ 현재 로그인된 사용자 정보 반환
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인되지 않음"));
        }

        String email = null;
        String provider;

        // 🌐 소셜 로그인 사용자
        if (auth.getPrincipal() instanceof OAuth2User oAuth2User) {
            var attributes = oAuth2User.getAttributes();
            email = (String) attributes.get("email");
            provider = String.valueOf(attributes.getOrDefault("provider", "SOCIAL"));
        }
        // 👤 일반 로그인 사용자
        else if (auth.getPrincipal() instanceof User user) {
            email = user.getEmail();
            provider = "LOCAL";
        } else {
            provider = "UNKNOWN";
        }

        // ❌ 이메일 누락 시
        if (email == null) {
            return ResponseEntity.status(400).body(Map.of("error", "이메일 정보를 가져올 수 없습니다."));
        }

        // ✅ DB에서 사용자 조회 후 반환
        return userRepository.findByEmail(email)
                .map(user -> ResponseEntity.ok(Map.of(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "nickname", user.getNickname(),
                        "email", user.getEmail(),
                        "provider", provider
                )))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "사용자 없음")));
    }

    /**
     * ✅ 닉네임 수정
     */
    @PutMapping("/update-nickname")
    public ResponseEntity<?> updateNickname(Authentication authentication, @RequestBody Map<String, String> request) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("로그인되지 않음");
        }

        String newNickname = request.get("nickname");
        if (newNickname == null || newNickname.isBlank()) {
            return ResponseEntity.badRequest().body("닉네임을 입력해주세요.");
        }

        String email = null;
        if (authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
            email = (String) oAuth2User.getAttributes().get("email");
        } else if (authentication.getPrincipal() instanceof User user) {
            email = user.getEmail();
        }

        if (email == null) {
            return ResponseEntity.status(400).body("이메일 정보를 찾을 수 없습니다.");
        }

        try {
            User updatedUser = authService.updateNickname(email, newNickname);
            return ResponseEntity.ok(Map.of("nickname", updatedUser.getNickname()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("닉네임 변경 실패: " + e.getMessage());
        }
    }

    /**
     * ✅ 회원 탈퇴 처리 (Soft Delete)
     */
    @DeleteMapping("/withdraw")
    public ResponseEntity<String> withdraw(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("로그인되지 않음");
        }

        String email = null;
        if (authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
            email = (String) oAuth2User.getAttributes().get("email");
        } else if (authentication.getPrincipal() instanceof User user) {
            email = user.getEmail();
        }

        if (email == null) {
            return ResponseEntity.badRequest().body("이메일 정보를 찾을 수 없습니다.");
        }

        authService.withdraw(email); // ✅ isDeleted = true 로 처리
        return ResponseEntity.ok("회원 탈퇴 처리 완료");
    }

    /**
     * ✅ 탈퇴 회원 복구
     */
    @PostMapping("/restore")
    public ResponseEntity<?> restoreUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            authService.restoreUser(email); // ✅ 탈퇴 유저 복구
            return ResponseEntity.ok("복구 성공");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
