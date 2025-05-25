package busking.busking_project;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // ✅ 이 클래스가 설정 클래스임을 명시
public class WebConfig {

    /**
     * ✅ WebMvcConfigurer를 사용해 CORS 설정을 커스터마이징
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            /**
             * ✅ 모든 경로에 대해 CORS 허용 설정
             */
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 모든 경로에 대해 CORS 적용
                        .allowedOrigins("http://localhost:3000") // 허용할 프론트엔드 주소 (React dev 서버)
                        .allowedMethods("*") // 모든 HTTP 메서드 허용 (GET, POST, PUT, DELETE 등)
                        .allowedHeaders("*") // 모든 요청 헤더 허용
                        .allowCredentials(true); // 인증 정보(Cookie, Authorization 등) 포함 허용
            }
        };
    }
}
