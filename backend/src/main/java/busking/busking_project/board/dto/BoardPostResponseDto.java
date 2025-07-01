package busking.busking_project.board.dto;

import busking.busking_project.board.BoardPost;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardPostResponseDto {
    private final Long id;
    private final String title;
    private final String content;
    private final Integer viewCount;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Long userId;           // 작성자 ID
    private final String nickname;       // 작성자 닉네임

    public BoardPostResponseDto(BoardPost entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.viewCount = entity.getViewCount();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
        this.userId = entity.getUser().getId();
        this.nickname = entity.getUser().getNickname();
    }
}
