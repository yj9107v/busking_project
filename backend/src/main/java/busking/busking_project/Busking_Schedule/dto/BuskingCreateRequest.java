// BuskingCreateRequest.java
package busking.busking_project.Busking_Schedule.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuskingCreateRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long locationId;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @Size(max = 100)
    private String description;
}

