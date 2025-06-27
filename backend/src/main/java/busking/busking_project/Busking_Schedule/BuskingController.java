package busking.busking_project.Busking_Schedule;

import busking.busking_project.Busking_Schedule.dto.BuskingCreateRequest;
import busking.busking_project.Busking_Schedule.dto.BuskingResponse;
import busking.busking_project.Busking_Schedule.dto.LocationWithScheduleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/busking")
@RequiredArgsConstructor
public class BuskingController {

    private final BuskingService buskingService;

    /**
     * 일정 등록
     */
    @PostMapping
    public ResponseEntity<?> createBusking(@RequestBody BuskingCreateRequest request) {
        try {
            log.info("✅ [일정 등록 요청] request: {}", request);
            BuskingResponse response = buskingService.create(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("❌ [일정 등록 실패]: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "일정 등록 실패", "message", e.getMessage())
            );
        }
    }

    /**
     * uuid로 단건 조회
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<BuskingResponse> getBuskingByUuid(@PathVariable String uuid) {
        BuskingResponse response = buskingService.getByUuid(uuid);
        return ResponseEntity.ok(response);
    }

    /**
     * 날짜로 전체 일정 조회
     */
    @GetMapping("/date")
    public ResponseEntity<List<BuskingResponse>> getBuskingsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<BuskingResponse> responses = buskingService.getByDate(date);
        return ResponseEntity.ok(responses);
    }

    /**
     * 모든 장소 + 일정 정보 반환 (지도/목록용)
     */
    @GetMapping("/locations")
    public ResponseEntity<List<LocationWithScheduleDto>> getLocationsWithSchedules() {
        List<LocationWithScheduleDto> schedules = buskingService.findAllWithLocationInfo();
        return ResponseEntity.ok(schedules);
    }
}
