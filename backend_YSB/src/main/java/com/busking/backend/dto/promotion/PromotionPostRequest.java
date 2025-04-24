package com.busking.backend.dto.promotion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromotionPostRequest {
    private String title;
    private String content;
    private String category; // 문자열로 받고, 서비스에서 enum으로 매핑
    private String mediaUrl;
}
