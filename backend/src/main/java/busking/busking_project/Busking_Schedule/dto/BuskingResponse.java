// BuskingResponse.java
package busking.busking_project.Busking_Schedule.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

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
    private String status;
}

