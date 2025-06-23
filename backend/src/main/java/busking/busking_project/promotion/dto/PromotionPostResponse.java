package busking.busking_project.promotion.dto;

import busking.busking_project.promotion.PromotionPost;
import lombok.Getter;

@Getter
public class PromotionPostResponse {

    private final Long id;
    private final Long userId;      // 작성자 PK
    private final String nickname;  // 작성자 닉네임(옵션)
    private final String title;
    private final String content;
    private final String mediaUrl;
    private final String category;
    private final Integer viewCount;
    private final String place;

    public PromotionPostResponse(PromotionPost entity) {
        this.id = entity.getId();
        this.userId = entity.getUserId().getId();                // User PK
        this.nickname = entity.getUserId().getNickname();        // User 닉네임 (원하면 추가)
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.mediaUrl = entity.getMediaUrl();
        this.category = entity.getCategory().name();
        this.viewCount = entity.getViewCount();
        this.place = entity.getPlace();
    }
}
