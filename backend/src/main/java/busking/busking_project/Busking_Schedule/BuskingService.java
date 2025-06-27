package busking.busking_project.Busking_Schedule;

import busking.busking_project.Busking_Schedule.dto.BuskingCreateRequest;
import busking.busking_project.Busking_Schedule.dto.BuskingResponse;
import busking.busking_project.Busking_Schedule.dto.LocationWithScheduleDto;
import busking.busking_project.location.Location;
import busking.busking_project.location.LocationRepository;
import busking.busking_project.user.User;
import busking.busking_project.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuskingService {

    private final BuskingRepository buskingRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    // ✅ 1. 일정 생성
    public BuskingResponse create(BuskingCreateRequest request) {
        if (isTimeOverlap(request.getLocationId(), request.getDate(), request.getStartTime(), request.getEndTime())) {
            throw new IllegalArgumentException("해당 시간에 이미 예약된 공연이 존재합니다.");
        }
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));
        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new RuntimeException("장소 정보를 찾을 수 없습니다."));
        BuskingSchedule busking = BuskingSchedule.builder()
                .uuid(UUID.randomUUID().toString())
                .user(user)
                .location(location)
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .description(request.getDescription())
                .status(BuskingStatus.SCHEDULED)
                .isDeleted(false)
                .build();
        buskingRepository.save(busking);
        return new BuskingResponse(busking); // DTO 변환 생성자 사용 추천
    }

    // ✅ 2. uuid로 단건 조회 (Soft Delete 반영)
    public BuskingResponse getByUuid(String uuid) {
        BuskingSchedule busking = buskingRepository.findByUuidAndIsDeletedFalse(uuid)
                .orElseThrow(() -> new RuntimeException("해당 일정이 존재하지 않습니다."));
        return new BuskingResponse(busking);
    }

    // ✅ 3. 날짜 기준 조회 (Soft Delete 반영)
    public List<BuskingResponse> getByDate(LocalDate date) {
        return buskingRepository.findByDateAndIsDeletedFalse(date).stream()
                .map(BuskingResponse::new)
                .collect(Collectors.toList());
    }

    // ✅ 4. 시간 겹침 중복 체크
    public boolean isTimeOverlap(Long locationId, LocalDate date, LocalTime start, LocalTime end) {
        return buskingRepository.existsByLocationAndDateAndTimeOverlap(locationId, date, start, end);
    }

    // ✅ 5. 일정 논리 삭제(Soft Delete)
    @Transactional
    public void deleteBusking(String uuid) {
        BuskingSchedule busking = buskingRepository.findByUuidAndIsDeletedFalse(uuid)
                .orElseThrow(() -> new RuntimeException("해당 일정이 존재하지 않습니다."));
        busking.setDeleted(true);
        busking.setUpdatedAt(LocalDateTime.now());
        buskingRepository.save(busking);
    }

    // ✅ 6. 매 1분마다 상태 자동 업데이트 (Soft Delete 제외)
    @Scheduled(cron = "0 * * * * *") // 매 1분마다
    @Transactional
    public void updateStatusAutomatically() {
        LocalDateTime now = LocalDateTime.now();
        List<BuskingSchedule> schedules = buskingRepository.findAll()
                .stream()
                .filter(b -> !b.isDeleted())
                .collect(Collectors.toList());
        for (BuskingSchedule busking : schedules) {
            LocalDateTime start = LocalDateTime.of(busking.getDate(), busking.getStartTime());
            LocalDateTime end = LocalDateTime.of(busking.getDate(), busking.getEndTime());
            if (now.isBefore(start)) {
                busking.setStatus(BuskingStatus.SCHEDULED);
            } else if (now.isAfter(end)) {
                busking.setStatus(BuskingStatus.COMPLETED);
            } else {
                busking.setStatus(BuskingStatus.ONGOING);
            }
        }
    }

    // ✅ 7. 장소 + 일정 정보 리스트 반환
    public List<LocationWithScheduleDto> findAllWithLocationInfo() {
        return buskingRepository.findAllWithLocationInfo();
    }
}
