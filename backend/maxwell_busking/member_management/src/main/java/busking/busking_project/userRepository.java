package busking.busking_project;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface userRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
    Optional<User> findBySocialIdAndProvider(String socialId, AuthProvider provider);
    // userRepository.java
    int deleteByIsDeletedTrueAndDeletedAtBefore(LocalDateTime time);

}
