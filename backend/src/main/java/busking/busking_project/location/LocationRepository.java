package busking.busking_project.location;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    // 논리 삭제(Soft Delete)된 데이터는 제외
    List<Location> findByIsDeletedFalse();
}
