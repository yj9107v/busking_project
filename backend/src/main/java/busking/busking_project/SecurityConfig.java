package busking.busking_project;

import busking.busking_project.user.OAuth2SuccessHandler;
import busking.busking_project.user.OAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.util.List;

@Configuration // ✅ 스프링 설정 클래스
@EnableWebSecurity // ✅ Spring Security 활성화
public class SecurityConfig {

    // 🔄 OAuth2 로그인 사용자 정보 처리 서비스 (구글/카카오 로그인 처리용)
    @Autowired
    private OAuth2UserService oAuth2UserService;

    // 🔄 소셜 로그인 성공 후 세션 저장 및 리디렉션 처리 핸들러
    @Autowired
    private OAuth2SuccessHandler oAuth2SuccessHandler;

    /**
     * ✅ 비밀번호 암호화를 위한 Bean 등록 (BCrypt 알고리즘 사용)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * ✅ CORS 설정
     * - React (3000포트)에서 API 요청 허용
     * - 인증정보(쿠키) 포함 허용
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 🔓 쿠키 전송 허용
        config.setAllowedOrigins(List.of("http://localhost:3000")); // 🔓 허용된 Origin
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With")); // 🔓 허용된 헤더
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 🔓 허용된 메서드
        config.setMaxAge(3600L); // 🔄 preflight 요청 캐시 시간 (1시간)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 모든 경로에 적용
        return source;
    }

    /**
     * ✅ Spring Security 설정 (필터 체인 구성)
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // ✅ 세션 기반 인증 정보 저장소 설정
                .securityContext(context -> context
                        .securityContextRepository(new HttpSessionSecurityContextRepository())
                )

                // ✅ CORS 설정 적용
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // ✅ CSRF 보호 비활성화 (SPA + API 방식에서는 일반적으로 사용 안 함)
                .csrf(csrf -> csrf.disable())

                // ✅ 세션 생성 정책 설정
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 요청 시 세션이 필요하면 생성
                )

                // ✅ 인증 예외 처리 핸들링 (401 응답 반환)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401); // 401 Unauthorized
                            response.setHeader(
                                    "WWW-Authenticate",
                                    "Bearer realm=\"api\", error=\"invalid_token\", error_description=\"Access token is expired\""
                            );
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"success\":false,\"message\":\"아이디 또는 비밀번호가 잘못되었습니다. 아이디와 비밀번호를 정확히 입력해주세요.\",\"errorCode\":\"USER_NOT_FOUND\",\"data\":null}");
                        })
                )

                // ✅ 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 🔓 정적 리소스, 로그인/로그아웃 경로, 리뷰 등은 허용
                        .requestMatchers(
                                "/", "/index.html", "/favicon.ico", "/manifest.json",
                                "/logo192.png", "/logo512.png", "/static/**",
                                "/oauth2/**", "/login/**", "/logout",
                                "/login-failed", "/login-success", "reviews"
                        ).permitAll()

                        // 🔓 회원가입 POST 요청 허용
                        .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()

                        // 🔓 모든 사용자 API 허용 (인증 없이도 /me 등 호출 가능)
                        .requestMatchers("/api/users/**").permitAll()

                        // 🔓 preflight 요청 허용 (OPTIONS 요청)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 🔒 그 외 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )

                // ✅ OAuth2 로그인 설정 (소셜 로그인)
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService)) // 사용자 정보 처리 서비스 지정

                        .successHandler(oAuth2SuccessHandler) // 로그인 성공 시 커스텀 핸들러
                        .failureUrl("http://localhost:3000/login-failed") // 실패 시 리디렉션
                )

                // ✅ 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 URL
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // JSESSIONID 쿠키 삭제
                        .logoutSuccessUrl("http://localhost:3000/") // 로그아웃 후 홈으로 이동
                        .permitAll()
                );

        return http.build();
    }
}
