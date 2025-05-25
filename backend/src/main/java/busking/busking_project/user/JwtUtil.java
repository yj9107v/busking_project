package busking.busking_project.user;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component // Spring Bean 등록 (다른 클래스에서 의존성 주입 가능)
public class JwtUtil {

    // ✅ JWT 서명을 위한 시크릿 키 (HS256용, 충분히 긴 문자열 필요)
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            "your-very-secret-key-that-is-long-enough".getBytes());

    // ✅ 액세스 토큰 유효 시간: 1시간 (ms 단위)
    private static final long ACCESS_EXPIRATION = 1000 * 60 * 60;

    // ✅ 리프레시 토큰 유효 시간: 7일 (ms 단위)
    private static final long REFRESH_EXPIRATION = 1000 * 60 * 60 * 24 * 7;

    /**
     * ✅ Access Token 생성
     * - subject: 토큰 주체 (보통 사용자 이메일이나 userId)
     * - return: 서명된 JWT 문자열
     */
    public static String generateAccessToken(String subject) {
        return Jwts.builder()
                .setSubject(subject) // 사용자 식별자 (email, id 등)
                .setIssuedAt(new Date()) // 발급 시각
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION)) // 만료 시각
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 서명 알고리즘 + 키
                .compact(); // 최종 JWT 생성
    }

    /**
     * ✅ Refresh Token 생성
     * - Access Token보다 긴 만료 시간을 가짐
     */
    public static String generateRefreshToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * ✅ 토큰에서 이메일(subject) 추출
     * - 서명 키를 검증한 후 파싱 수행
     * - 유효하지 않으면 null 반환
     */
    public String extractEmail(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY) // 서명 키 설정
                    .build()
                    .parseClaimsJws(token) // 토큰 파싱 및 검증
                    .getBody()
                    .getSubject(); // 토큰 주체(subject) 반환 (이메일 또는 ID)
        } catch (JwtException e) {
            // 서명 실패, 만료 등 오류 발생 시 로그 출력 후 null 반환
            System.err.println("JWT 파싱 오류: " + e.getMessage());
            return null;
        }
    }
}
