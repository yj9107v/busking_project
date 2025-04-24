package com.busking.backend.dto.board;

import com.busking.backend.domain.board.BoardPost;
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

    public BoardPostResponseDto(BoardPost entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.viewCount = entity.getViewCount();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
