package busking.busking_project.promotion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromotionPostRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 100, message = "제목은 100자 이하로 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotBlank(message = "카테고리를 입력해주세요.")
    @Pattern(regexp = "ART|DANCE|MUSIC|TALK", message = "유효한 카테고리(ART, DANCE, MUSIC, TALK)만 허용됩니다.")
    private String category;

    private String mediaUrl;  // 선택 항목

    @NotBlank(message = "장소를 입력해주세요.")
    private String place;
}
