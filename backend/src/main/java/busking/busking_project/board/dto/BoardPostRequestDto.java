package busking.busking_project.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardPostRequestDto {
    private String title;
    private String content;
    private Long userId;
}
