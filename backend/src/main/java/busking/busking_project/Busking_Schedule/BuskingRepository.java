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

    // ✅ uuid로 단건 조회
    Optional<BuskingSchedule> findByUuid(String uuid);

    // ✅ 날짜로 전체 리스트 조회
    List<BuskingSchedule> findByDate(LocalDate date);

    // ✅ 특정 유저의 일정 조회
    List<BuskingSchedule> findByUser_Id(Long userId); // ManyToOne 연관 필드로 수정됨

    // ✅ 날짜 + 장소 기준 중복 방지
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

    // ✅ 모든 일정 + 장소 정보 반환
    @Query("""
        SELECT new busking.busking_project.Busking_Schedule.dto.LocationWithScheduleDto(
            l.id, l.name, l.latitude, l.longitude, l.description,
            s.date, s.startTime, s.endTime
        )
        FROM BuskingSchedule s
        JOIN s.location l
        WHERE s.isDeleted = false
    """)
    List<LocationWithScheduleDto> findAllWithLocationInfo();
}
