package busking.busking_project.Busking_Schedule;

import busking.busking_project.Busking_Schedule.dto.LocationWithScheduleDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface BuskingRepository extends JpaRepository<BuskingSchedule, Long> {

    // ✅ Soft Delete 적용 버전 추가
    Optional<BuskingSchedule> findByUuidAndIsDeletedFalse(String uuid);

    List<BuskingSchedule> findByDateAndIsDeletedFalse(LocalDate date);

    List<BuskingSchedule> findByUser_IdAndIsDeletedFalse(Long userId);

    // (기존) uuid, 날짜, 유저로도 조회 가능
    Optional<BuskingSchedule> findByUuid(String uuid); // 이 경우도 isDeleted false로 조회 권장

    // ✅ 시간 겹침 중복 체크 (isDeleted false 이미 적용)
    @Query("""
        SELECT COUNT(s) > 0 FROM BuskingSchedule s
        WHERE s.location.id = :locationId
          AND s.date = :date
          AND s.isDeleted = false
          AND (
              (s.startTime < :endTime AND s.endTime > :startTime)
          )
    """)
    boolean existsByLocationAndDateAndTimeOverlap(@Param("locationId") Long locationId,
                                                  @Param("date") LocalDate date,
                                                  @Param("startTime") LocalTime startTime,
                                                  @Param("endTime") LocalTime endTime);

    // ✅ 모든 일정+장소 DTO (추가 필드 필요시 DTO/JPQL 쿼리 수정)
    @Query("""
        SELECT new busking.busking_project.Busking_Schedule.dto.LocationWithScheduleDto(
            l.id, l.name, l.latitude, l.longitude,
            s.date, s.startTime, s.endTime, s.description,
            s.status, s.isDeleted, s.uuid
        )
        FROM BuskingSchedule s
        JOIN s.location l
        WHERE s.isDeleted = false
    """)
    List<LocationWithScheduleDto> findAllWithLocationInfo();
}
