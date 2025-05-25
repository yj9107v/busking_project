// UserCleanupScheduler.java
package busking.busking_project.user;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component // 스프링 빈으로 등록되는 컴포넌트 (스케줄러로 사용됨)
public class UserCleanupScheduler {

    private final UserRepository userRepository;

    // 생성자 주입 방식으로 UserRepository 의존성 주입
    public UserCleanupScheduler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 🔁 매 1분마다 실행되는 스케줄러 (테스트 용도)
     * cron 표현식: "0 * * * * *" → 매 분 0초마다 실행
     * 실 운영에서는 예: "0 0 3 * * *" → 매일 새벽 3시 정각 등으로 설정
     */
    @Scheduled(cron = "0 * * * * *")
    @Transactional // 트랜잭션 처리: delete 쿼리를 안전하게 실행
    public void deleteExpiredUsers() {
        // 삭제 기준 시간: 현재 시각에서 1분 전 (테스트용 기준)
        LocalDateTime expired = LocalDateTime.now().minusMinutes(1);

        // 조건에 맞는 소프트 삭제 사용자들을 실제 DB에서 제거
        int count = userRepository.deleteByIsDeletedTrueAndDeletedAtBefore(expired);

        // 콘솔에 삭제된 유저 수 출력 (로그 기록)
        System.out.println("🧹 삭제된 유저 수: " + count);
    }
}
