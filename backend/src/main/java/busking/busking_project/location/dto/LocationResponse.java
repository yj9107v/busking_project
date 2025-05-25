// LocationResponse.java
package busking.busking_project.location.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LocationResponse {
    private Long id;
    private String name;
    private double latitude;
    private double longitude;
    private String region;
    private String description;
    private boolean isActive;
}

