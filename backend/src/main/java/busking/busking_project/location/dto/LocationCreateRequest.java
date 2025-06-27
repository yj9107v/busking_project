package busking.busking_project.location.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * 장소 등록 요청 DTO
 * - 입력값 검증, 설명 필드 길이 제한 등 필요에 따라 보완 가능
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationCreateRequest {

    @NotBlank(message = "장소 이름을 입력하세요.")
    @Size(max = 100, message = "장소 이름은 100자 이내로 입력하세요.")
    private String name;

    private Double latitude;

    private Double longitude;

    @NotBlank(message = "지역명을 입력하세요.")
    @Size(max = 50, message = "지역명은 50자 이내로 입력하세요.")
    private String region;

    private String description;
}
