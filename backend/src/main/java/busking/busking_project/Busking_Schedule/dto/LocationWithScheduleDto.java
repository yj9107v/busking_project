package busking.busking_project.Busking_Schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 장소 + 버스킹 일정 DTO
 * - 특정 장소에 등록된 공연(스케줄) 정보를 함께 반환할 때 사용
 */
@Getter
@AllArgsConstructor
public class LocationWithScheduleDto {
    private Long locationId;
    private String name;
    private Double latitude;
    private Double longitude;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String description;
    private String status;
    private boolean isDeleted;
    private String uuid;
}
