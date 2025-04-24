package com.busking.backend.controller;

import com.busking.backend.dto.promotion.PromotionPostRequest;
import com.busking.backend.dto.promotion.PromotionPostResponse;
import com.busking.backend.service.PromotionPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/promotions")
public class PromotionPostController {

    private final PromotionPostService promotionPostService;

    @GetMapping
    public ResponseEntity<List<PromotionPostResponse>> getAllPosts() {
        return ResponseEntity.ok(promotionPostService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionPostResponse> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(promotionPostService.getPostById(id));
    }

    @PostMapping
    public ResponseEntity<PromotionPostResponse> createPost(@RequestBody PromotionPostRequest request) {
        PromotionPostResponse created = promotionPostService.createPromotionPost(request);
        return ResponseEntity.created(URI.create("/api/promotions/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromotionPostResponse> updatePost(@PathVariable Long id,
                                                            @RequestBody PromotionPostRequest request) {
        PromotionPostResponse updated = promotionPostService.updatePromotionPost(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        promotionPostService.deletePromotionPost(id);
        return ResponseEntity.noContent().build();
    }
}