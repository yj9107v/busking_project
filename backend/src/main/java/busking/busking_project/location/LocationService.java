package busking.busking_project.location;

import busking.busking_project.location.dto.LocationCreateRequest;
import busking.busking_project.location.dto.LocationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    /**
     * 장소 등록 (Entity → DTO로 반환)
     */
    public LocationResponse createLocation(LocationCreateRequest request) {
        Location location = Location.builder()
                .name(request.getName())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .region(request.getRegion())
                .description(request.getDescription())
                .isActive(true)
                .isDeleted(false) // Soft Delete 기본값
                .build();

        locationRepository.save(location);
        return new LocationResponse(location); // 엔티티 → DTO 변환자 활용!
    }

    /**
     * 전체 장소 목록(Soft Delete 제외)
     */
    public List<LocationResponse> getAllLocations() {
        return locationRepository.findByIsDeletedFalse().stream()
                .map(LocationResponse::new)
                .toList();
    }

    /**
     * 장소 논리 삭제(Soft Delete)
     */
    public void softDeleteLocation(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장소입니다."));
        location.setDeleted(true); // Soft Delete
        location.setActive(false); // 운영 비활성화
        locationRepository.save(location);
    }
}
