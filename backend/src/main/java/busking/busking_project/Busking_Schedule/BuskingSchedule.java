package busking.busking_project.Busking_Schedule;

import busking.busking_project.location.Location;
import busking.busking_project.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "busking_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuskingSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ 외부 공개용 UUID
    @Column(nullable = false, unique = true, updatable = false, length = 36)
    private String uuid;

    // ✅ 버스커 (User FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ✅ 공연 장소 (Location FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    // ✅ 날짜 및 시간
    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    // ✅ 공연 설명
    @Column(length = 100)
    private String description;

    // ✅ 상태 ENUM: SCHEDULED / ONGOING / COMPLETED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BuskingStatus status;

    // ✅ 생성/수정일, 삭제 상태
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    // ✅ 생성 시 자동 필드 설정
    @PrePersist
    public void onCreate() {
        this.uuid = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = BuskingStatus.SCHEDULED;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
