package busking.busking_project.user.jwt;

import busking.busking_project.user.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    // 굳이 주입받지 않아도 되지만, 향후 테스트용 구현 교체를 위해 분리
    private final JwtUtil jwtUtil;

    public String createAccessToken(long userId) {
        return JwtUtil.generateAccessToken(String.valueOf(userId));
    }

    public String createRefreshToken(long userId) {
        return JwtUtil.generateRefreshToken(String.valueOf(userId));
    }
}
