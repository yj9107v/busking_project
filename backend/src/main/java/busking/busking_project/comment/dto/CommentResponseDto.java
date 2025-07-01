package busking.busking_project.comment.dto;

import busking.busking_project.comment.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private final Long id;
    private final Long postId;     // BoardPost PK
    private final Long userId;     // User PK
    private final String content;
    private final Long parentId;   // 부모 댓글 PK (nullable)
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.postId = comment.getPostId().getId();   // 객체에서 PK 추출
        this.userId = comment.getUser().getId();     // 객체에서 PK 추출
        this.content = comment.getContent();
        this.parentId = comment.getParent() != null ? comment.getParent().getId() : null; // null-safe
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}
