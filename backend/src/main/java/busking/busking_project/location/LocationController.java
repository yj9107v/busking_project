package busking.busking_project.location;

import busking.busking_project.location.dto.LocationCreateRequest;
import busking.busking_project.location.dto.LocationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class LocationController {

    private final LocationService locationService;

    /**
     * 장소 등록
     */
    @PostMapping
    public ResponseEntity<LocationResponse> createLocation(@Valid @RequestBody LocationCreateRequest request) {
        LocationResponse location = locationService.createLocation(request);
        return ResponseEntity.ok(location);
    }

    /**
     * 전체 장소 목록(Soft Delete 제외)
     */
    @GetMapping
    public ResponseEntity<List<LocationResponse>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    /**
     * 장소 논리 삭제(Soft Delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLocation(@PathVariable Long id) {
        locationService.softDeleteLocation(id);
        return ResponseEntity.ok().build();
    }
}
