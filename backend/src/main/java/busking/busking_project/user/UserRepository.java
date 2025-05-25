package busking.busking_project.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

// User 엔티티에 대한 데이터베이스 접근을 처리하는 Repository 인터페이스
public interface UserRepository extends JpaRepository<User, Long> {

    // username으로 사용자 조회 (로그인 등에서 사용)
    Optional<User> findByUsername(String username);

    // email로 사용자 조회 (이메일 중복 확인, 비밀번호 찾기 등에서 사용)
    Optional<User> findByEmail(String email);

    // nickname으로 사용자 조회 (닉네임 중복 확인 등에서 사용)
    Optional<User> findByNickname(String nickname);

    // 소셜 로그인 시, 소셜 ID와 제공자를 통해 사용자 조회
    Optional<User> findBySocialIdAndProvider(String socialId, AuthProvider provider);

    // 계정이 삭제된 사용자 중 삭제 시간(deletedAt)이 특정 시간 이전인 사용자들을 완전 삭제
    // 주로 스케줄러에서 주기적으로 호출하여 DB 정리
    int deleteByIsDeletedTrueAndDeletedAtBefore(LocalDateTime time);
}
