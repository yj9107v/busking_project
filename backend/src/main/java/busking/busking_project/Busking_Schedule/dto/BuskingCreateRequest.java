package busking.busking_project.Busking_Schedule.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 버스킹 일정 생성 요청 DTO
 * - BuskingSchedule 생성에 필요한 입력값만 포함
 * - 상태(status), uuid, isDeleted 등은 엔티티/서비스에서 자동 처리
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuskingCreateRequest {

    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long userId;

    @NotNull(message = "장소 ID는 필수입니다.")
    private Long locationId;

    @NotNull(message = "공연 날짜를 입력하세요.")
    private LocalDate date;

    @NotNull(message = "공연 시작 시간을 입력하세요.")
    private LocalTime startTime;

    @NotNull(message = "공연 종료 시간을 입력하세요.")
    private LocalTime endTime;

    @Size(max = 100, message = "공연 설명은 100자 이내로 입력하세요.")
    private String description;
}
