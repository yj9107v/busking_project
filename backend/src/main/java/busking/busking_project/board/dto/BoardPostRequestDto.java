package busking.busking_project.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 게시글 등록/수정 요청 DTO
 */
@Getter
@Setter
public class BoardPostRequestDto {
    @NotBlank(message = "제목을 입력하세요.")
    @Size(max = 100, message = "제목은 100자 이내로 입력하세요.")
    private String title;

    @NotBlank(message = "내용을 입력하세요.")
    private String content;

    @NotBlank(message = "작성자 정보가 누락되었습니다.")
    private Long userId;
}
