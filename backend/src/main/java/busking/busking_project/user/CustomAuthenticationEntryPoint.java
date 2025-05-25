package busking.busking_project.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component // ✅ 스프링이 이 클래스를 빈으로 등록하도록 설정
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * ✅ 인증 실패 시 호출되는 메서드
     * - 인증되지 않은 사용자가 보호된 API에 접근할 경우 실행됨
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // 응답 타입 설정 (JSON, UTF-8 인코딩 명시)
        response.setContentType("application/json;charset=UTF-8");

        // HTTP 상태 코드 설정: 401 Unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 실제 반환할 JSON 에러 메시지 작성
        response.getWriter().write("{\"error\": \"로그인되지 않음\"}");
    }
}
