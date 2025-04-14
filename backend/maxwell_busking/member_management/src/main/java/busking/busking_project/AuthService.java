package busking.busking_project;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthService {

    @Autowired private userRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    /**
     * ✅ 회원가입 처리
     */
    public void register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("이미 존재하는 ID입니다.");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .nickname(request.getNickname())
                .role(Role.USER)
                .provider(AuthProvider.LOCAL)
                .isDeleted(false)
                .build();

        userRepository.save(user);
    }

    /**
     * ✅ 로그인 + 세션 저장 처리
     */
    public String login(String username, String password, HttpServletRequest request) {
        User user = userRepository.findByUsername(username)
                .filter(u -> !u.isDeleted())  // 🔒 삭제된 유저는 로그인 불가
                .orElseThrow(() -> new RuntimeException("존재하지 않거나 탈퇴한 사용자입니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        request.getSession(true).setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context
        );

        return "로그인 성공 (세션 생성 완료)";
    }


    // 닉네임 변경
    public User updateNickname(String email, String newNickname) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        user.setNickname(newNickname);
        return userRepository.save(user);
    }

    // 회원 탈퇴 처리
    public void withdraw(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        user.setDeleted(true);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public void restoreUser(String email) {
        User user = userRepository.findByEmail(email)
                .filter(User::isDeleted)
                .orElseThrow(() -> new RuntimeException("삭제된 유저가 아닙니다."));

        if (user.getDeletedAt().isBefore(LocalDateTime.now().minusMinutes(1))) {
            throw new RuntimeException("복구 가능 기간이 지났습니다.");
        }

        user.setDeleted(false);
        user.setDeletedAt(null);
        userRepository.save(user);
    }
}
