package busking.busking_project.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto {

    @NotNull
    private Long postId;

    @NotNull
    private Long userId;

    @NotBlank(message = "댓글 내용은 필수 입력값입니다.")
    private String comment;

    @Min(1)
    @Max(5)
    private int rating;
}
