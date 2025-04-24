package com.busking.backend.dto.promotion;

import com.busking.backend.domain.promotion.PromotionPost;
import lombok.Getter;

@Getter
public class PromotionPostResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String mediaUrl;
    private final String category;
    private final Integer viewCount;

    public PromotionPostResponse(PromotionPost entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.mediaUrl = entity.getMediaUrl();
        this.category = entity.getCategory().name(); // Enum → 문자열
        this.viewCount = entity.getViewCount();
    }
}