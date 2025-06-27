package busking.busking_project.Busking_Schedule.dto;

import busking.busking_project.Busking_Schedule.BuskingSchedule;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 버스킹 일정 조회 응답 DTO
 * - 엔티티에서 필요한 정보만 추출해서 프론트로 전달
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuskingResponse {

    private String uuid;
    private Long userId;
    private Long locationId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String description;
    private String status;        // SCHEDULED / ONGOING / COMPLETED
    private boolean isDeleted;    // Soft Delete 적용 여부
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * BuskingSchedule 엔티티 → DTO 변환 생성자
     */
    public BuskingResponse(BuskingSchedule schedule) {
        this.uuid = schedule.getUuid();
        this.userId = schedule.getUser().getId();
        this.locationId = schedule.getLocation().getId();
        this.date = schedule.getDate();
        this.startTime = schedule.getStartTime();
        this.endTime = schedule.getEndTime();
        this.description = schedule.getDescription();
        this.status = schedule.getStatus() != null ? schedule.getStatus().name() : null;
        this.isDeleted = schedule.isDeleted();
        this.createdAt = schedule.getCreatedAt();
        this.updatedAt = schedule.getUpdatedAt();
    }
}
