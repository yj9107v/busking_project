package busking.busking_project.user.entity;

import busking.busking_project.user.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.*;

@DataJpaTest
@ActiveProfiles("test")
class UserEntityTest {

    @Autowired
    private UserRepository userRepository;

    // ✨ 1) 빌더가 기본값 · PrePersist 확인
    @Test
    void builder_shouldSetDefaultsAndTimestamps() {
        User saved = userRepository.save(User.builder()
                .username("user02")
                .password("encodedPw")
                .email("u2@example.com")
                .nickname("닉네임2")
                .provider(AuthProvider.LOCAL)
                .role(Role.USER)
                .build());

        assertThat(saved.isDeleted()).isFalse();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    // ✨ 2) username/email unique 제약 위반
    @Test
    void duplicateUsername_shouldFail() {
        userRepository.save(dummy("dup", "dup@example.com"));
        User dup = dummy("dup", "another@example.com");

        assertThatThrownBy(() -> userRepository.saveAndFlush(dup))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("constraint");
    }

    // ✨ 3) soft-delete 플래그가 기본 조회에 포함되는지(커스텀 쿼리 존재 시) …

    // --- 헬퍼 메서드 ---
    private User dummy(String username, String email) {
        return User.builder()
                .username(username)
                .password("pw")
                .email(email)
                .nickname("닉")
                .provider(AuthProvider.LOCAL)
                .role(Role.USER)
                .build();
    }
}
