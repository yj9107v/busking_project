package busking.busking_project.Busking_Schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;
import java.time.LocalTime;

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
}