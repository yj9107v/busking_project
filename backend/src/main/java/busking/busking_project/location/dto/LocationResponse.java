package busking.busking_project.location.dto;

import busking.busking_project.location.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 장소 정보 응답 DTO
 */
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
    private boolean isDeleted;           // Soft Delete
    private LocalDateTime createdAt;     // 생성일
    private LocalDateTime updatedAt;     // 수정일

    /**
     * Location 엔티티 → DTO 변환 생성자
     */
    public LocationResponse(Location location) {
        this.id = location.getId();
        this.name = location.getName();
        this.latitude = location.getLatitude() != null ? location.getLatitude() : 0.0;
        this.longitude = location.getLongitude() != null ? location.getLongitude() : 0.0;
        this.region = location.getRegion();
        this.description = location.getDescription();
        this.isActive = location.isActive();
        this.isDeleted = location.isDeleted();
        this.createdAt = location.getCreatedAt();
        this.updatedAt = location.getUpdatedAt();
    }
}
