package busking.busking_project.location;

import busking.busking_project.location.dto.LocationCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import busking.busking_project.location.dto.LocationResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public Location createLocation(LocationCreateRequest request) {
        Location location = Location.builder()
                .name(request.getName())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .region(request.getRegion())
                .description(request.getDescription())
                .isActive(true)
                .build();

        return locationRepository.save(location);
    }


    public List<LocationResponse> getAllLocations() {
        return locationRepository.findAll()
                .stream()
                .map(location -> LocationResponse.builder()
                        .id(location.getId())
                        .name(location.getName())
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .region(location.getRegion())
                        .description(location.getDescription())
                        .isActive(location.isActive())
                        .build())
                .toList();
    }

    public void deleteLocation(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장소입니다."));
        locationRepository.delete(location);
    }


}
