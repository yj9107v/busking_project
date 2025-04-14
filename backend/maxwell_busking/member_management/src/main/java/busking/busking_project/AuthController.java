package busking.busking_project;

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
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthController {

    @Autowired private AuthService authService;
    @Autowired private userRepository userRepository;

    /**
     * ✅ 회원가입 (유효성 검사 포함)
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(Map.of("error", message));
        }

        try {
            authService.register(request);
            return ResponseEntity.ok(Map.of("message", "회원가입 성공"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * ✅ 일반 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> user,
                                   HttpServletRequest request) {
        try {
            String token = authService.login(user.get("username"), user.get("password"), request);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * ✅ 소셜 로그인 성공 후 프론트 리디렉션
     */
    @GetMapping("/login-success")
    public void loginSuccess(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:3000");
    }

    /**
     * ✅ 현재 로그인된 사용자 정보 조회
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인되지 않음"));
        }

        String email = null;
        String provider;

        if (auth.getPrincipal() instanceof OAuth2User oAuth2User) {
            var attributes = oAuth2User.getAttributes();
            email = (String) attributes.get("email");
            provider = String.valueOf(attributes.getOrDefault("provider", "SOCIAL"));
        } else if (auth.getPrincipal() instanceof User user) {
            email = user.getEmail();
            provider = "LOCAL";
        } else {
            provider = "UNKNOWN";
        }

        if (email == null) {
            return ResponseEntity.status(400).body(Map.of("error", "이메일 정보를 가져올 수 없습니다."));
        }

        return userRepository.findByEmail(email)
                .map(user -> ResponseEntity.ok(Map.of(
                        "username", user.getUsername(),
                        "nickname", user.getNickname(),
                        "email", user.getEmail(),
                        "provider", provider
                )))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "사용자 없음")));
    }

    @PutMapping("/update-nickname")
    public ResponseEntity<?> updateNickname(
            Authentication authentication,
            @RequestBody Map<String, String> request
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("로그인되지 않음");
        }

        String newNickname = request.get("nickname");
        if (newNickname == null || newNickname.isBlank()) {
            return ResponseEntity.badRequest().body("닉네임을 입력해주세요.");
        }

        // 이메일 추출
        String email = null;

        if (authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
            email = (String) oAuth2User.getAttributes().get("email");
        } else if (authentication.getPrincipal() instanceof User user) {
            email = user.getEmail();
        }

        if (email == null) {
            return ResponseEntity.status(400).body("이메일 정보를 찾을 수 없습니다.");
        }

        // 닉네임 변경
        try {
            User updatedUser = authService.updateNickname(email, newNickname);
            return ResponseEntity.ok(Map.of(
                    "nickname", updatedUser.getNickname()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("닉네임 변경 실패: " + e.getMessage());
        }
    }


    @DeleteMapping("/withdraw")
    public ResponseEntity<String> withdraw(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("로그인되지 않음");
        }

        String email = null;

        // 소셜 로그인
        if (authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
            email = (String) oAuth2User.getAttributes().get("email");
        }
        // 일반 로그인
        else if (authentication.getPrincipal() instanceof User user) {
            email = user.getEmail();
        }

        if (email == null) {
            return ResponseEntity.badRequest().body("이메일 정보를 찾을 수 없습니다.");
        }

        authService.withdraw(email);
        return ResponseEntity.ok("회원 탈퇴 처리 완료");
    }

    // AuthController.java
    @PostMapping("/restore")
    public ResponseEntity<?> restoreUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            authService.restoreUser(email);
            return ResponseEntity.ok("복구 성공");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}
