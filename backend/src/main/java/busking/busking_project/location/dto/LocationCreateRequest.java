package busking.busking_project.location.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationCreateRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    private Double latitude;

    private Double longitude;

    @NotBlank
    @Size(max = 50)
    private String region;

    private String description;


}
