package com.busking.backend.dto.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    private Long postId;
    private Long userId;
    private String content;
    private Long parentId; // 대댓글용 (nullable)
}