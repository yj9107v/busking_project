package busking.busking_project.location;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name; // 장소 이름 (ex. 홍대 걷고 싶은 거리)

    @Column
    private Double latitude; // 위도

    @Column
    private Double longitude; // 경도

    @Column(nullable = false, length = 50)
    private String region; // 지역명 (예: 서울)

    @Column(columnDefinition = "TEXT")
    private String description; // 장소 설명

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true; // 장소 사용 여부 (기본 true)
}
