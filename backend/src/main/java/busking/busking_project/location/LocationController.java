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

    @PostMapping
    public ResponseEntity<?> createLocation(@Valid @RequestBody LocationCreateRequest request) {
        Location location = locationService.createLocation(request);
        return ResponseEntity.ok(location);
    }

    // LocationController.java
    @GetMapping
    public ResponseEntity<List<LocationResponse>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.ok().build();
    }

}
