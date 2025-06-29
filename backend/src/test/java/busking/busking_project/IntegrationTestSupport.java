package busking.busking_project;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * 📌 모든 통합 테스트가 상속해서 쓰는 공통 부모 클래스
 */
@SpringBootTest                    // 스프링 부트 전체 컨텍스트 기동
@AutoConfigureMockMvc              // MockMvc 자동 설정
@ActiveProfiles("test")            // application-test.yml 사용 (H2 메모리 DB)
@Transactional                     // 각 테스트 종료 후 rollback
public abstract class IntegrationTestSupport {

    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
}