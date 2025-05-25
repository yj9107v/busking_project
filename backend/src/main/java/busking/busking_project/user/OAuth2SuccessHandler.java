package busking.busking_project.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component // 스프링 컴포넌트로 등록
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    /**
     * 소셜 로그인 인증 성공 시 호출되는 메서드
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // ✅ SecurityContext 생성 및 인증 정보 설정
        var context = SecurityContextHolder.createEmptyContext(); // 빈 컨텍스트 생성
        context.setAuthentication(authentication);                 // 인증 객체 설정
        SecurityContextHolder.setContext(context);                // 현재 SecurityContextHolder에 저장

        // ✅ 세션에 SecurityContext 저장 (세션 로그인 유지)
        request.getSession(true).setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                context
        );

        // ✅ 로그인 성공 후 프론트엔드 메인 페이지로 리디렉션
        response.sendRedirect("http://localhost:3000"); // 예: 로그인 후 메인 홈
    }
}
