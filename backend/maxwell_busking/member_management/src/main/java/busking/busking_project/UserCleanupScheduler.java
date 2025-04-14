// UserCleanupScheduler.java
package busking.busking_project;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class UserCleanupScheduler {

    private final userRepository userRepository;

    public UserCleanupScheduler(userRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 * * * * *") // 🔁 매 1분마다 테스트용 (cron: 초 분 시 일 월 요일)
    @Transactional
    public void deleteExpiredUsers() {
        LocalDateTime expired = LocalDateTime.now().minusMinutes(1);
        int count = userRepository.deleteByIsDeletedTrueAndDeletedAtBefore(expired);
        System.out.println("🧹 삭제된 유저 수: " + count);
    }
}

