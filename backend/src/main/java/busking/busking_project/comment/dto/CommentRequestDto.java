package busking.busking_project.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    @NotNull(message = "게시글 ID는 필수입니다.")
    private Long postId;

    @NotNull(message = "작성자(사용자) ID는 필수입니다.")
    private Long userId;

    @NotBlank(message = "댓글 내용을 입력하세요.")
    private String content;

    private Long parentId; // 대댓글용 (nullable)
}
