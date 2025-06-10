package busking.busking_project.user;

import busking.busking_project.user.dto.LoginResponseDto;
import busking.busking_project.user.dto.RegisterRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    /**
     * ✅ 회원가입 처리 및 저장된 유저 반환
     */
    @Transactional
    public User register(RegisterRequestDto request) {
        // 아이디 중복 검사
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("USER_USERNAME_DUPLICATED"); // 에러코드로 구분 가능
        }

        // 이메일 중복 검사
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("USER_EMAIL_DUPLICATED");
        }

        // 닉네임 중복 검사
        if (userRepository.findByNickname(request.getNickname()).isPresent()) {
            throw new CustomException("이미 사용 중인 닉네임입니다.", "USER_NICKNAME_DUPLICATED");
        }

        // 사용자 객체 생성 및 저장
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword())) // 비밀번호 암호화
                .email(request.getEmail())
                .nickname(request.getNickname())
                .role(Role.USER)
                .provider(AuthProvider.LOCAL)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        // 즉시 DB에 반영되도록 flush()
        userRepository.flush();

        return savedUser;
    }

    /**
     * ✅ 로그인 + 세션 저장 처리 (세션 기반 인증)
     */
    public String login(String username, String password, HttpServletRequest request) {
        // 사용자 조회 및 삭제되지 않은지 확인
        User user = userRepository.findByUsername(username)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new RuntimeException("존재하지 않거나 탈퇴한 사용자입니다."));

        // 비밀번호 비교
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("USER_NOT_FOUND");
        }

        // SecurityContext에 인증 정보 설정
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        // 세션에 SecurityContext 저장
        request.getSession(true).setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context
        );

        return "로그인 성공 (세션 생성 완료)";
    }

    /**
     * ✅ 사용자 닉네임 수정
     */
    public User updateNickname(String email, String newNickname) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        user.setNickname(newNickname);
        return userRepository.save(user);
    }

    /**
     * ✅ 회원 탈퇴 처리 (Soft Delete)
     */
    public void withdraw(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        user.setDeleted(true); // isDeleted = true
        user.setDeletedAt(LocalDateTime.now()); // 삭제 시간 기록
        userRepository.save(user);
    }

    /**
     * ✅ 탈퇴 회원 복구
     * - 삭제 후 일정 시간이 지나면 복구 불가
     */
    public void restoreUser(String email) {
        User user = userRepository.findByEmail(email)
                .filter(User::isDeleted) // 삭제된 사용자만 복구 대상
                .orElseThrow(() -> new RuntimeException("삭제된 유저가 아닙니다."));

        // 복구 가능 시간 초과 검사
        if (user.getDeletedAt().isBefore(LocalDateTime.now().minusMinutes(1))) {
            throw new RuntimeException("복구 가능 기간이 지났습니다.");
        }

        user.setDeleted(false);     // isDeleted = false
        user.setDeletedAt(null);    // 복구했으므로 삭제 시간 초기화
        userRepository.save(user);
    }

    /**
     * ✅ 로그인 후 JWT 토큰(Access + Refresh) 반환
     */
    public LoginResponseDto loginWithTokens(String username, String password, HttpServletRequest request) {
        User user = userRepository.findByUsername(username)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("USER_NOT_FOUND");
        }

        // 세션 기반 인증 처리
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

        // JWT 토큰 생성
        String accessToken = JwtUtil.generateAccessToken(user.getUsername());
        String refreshToken = JwtUtil.generateRefreshToken(user.getUsername());

        // 사용자 정보 + 토큰 반환
        return new LoginResponseDto(accessToken, refreshToken, user);
    }
}
