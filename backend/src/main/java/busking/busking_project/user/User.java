package busking.busking_project.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity // JPA 엔티티임을 명시
@Table(name = "users") // DB 테이블 이름을 "users"로 지정
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    // 사용자 정보를 저장하는 JPA 엔티티 클래스

    @Id // 기본 키 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 전략 (MySQL 기준 AUTO_INCREMENT)
    private Long id;

    @Column(nullable = false, unique = true, length = 20) // Not null + 유일값 제약 + 길이 제한
    private String username; // 로그인용 ID

    @Column(length = 255) // 비밀번호 (암호화된 값 저장을 위한 길이 설정)
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String email; // 사용자 이메일

    @Column(nullable = false, unique = true, length = 30)
    private String nickname; // 사용자 닉네임

    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    @Column(nullable = false, length = 20)
    private AuthProvider provider; // 로그인 제공자 (GOOGLE, KAKAO, LOCAL)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role; // 사용자 역할 (USER), Admin은 mysql에서 따로 설정할 것!

    @Column(nullable = false)
    private LocalDateTime createdAt; // 계정 생성 시간

    @Column
    private LocalDateTime updatedAt; // 마지막 수정 시간

    @Builder.Default
    @Column(nullable = false)
    private boolean isDeleted = false; // 계정 삭제 여부 (기본값 false)

    private LocalDateTime deletedAt; // 삭제된 경우 시간 기록

    @Column(name = "social_id", unique = true)
    private String socialId; // 소셜 로그인 사용자의 고유 ID

    // 엔티티 저장 전 자동 실행되는 메서드
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now(); // 생성 시간 설정
        this.updatedAt = LocalDateTime.now(); // 최초 생성 시 수정 시간도 함께 설정
    }

    // 엔티티 업데이트 전 자동 실행되는 메서드
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now(); // 수정 시간 갱신
    }
}

