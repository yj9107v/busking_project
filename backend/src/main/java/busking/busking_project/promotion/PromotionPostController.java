package busking.busking_project.promotion;

import busking.busking_project.promotion.dto.PromotionPostRequest;
import busking.busking_project.promotion.dto.PromotionPostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/promotions")
public class PromotionPostController {

    private final PromotionPostService promotionPostService;

    @GetMapping
    public ResponseEntity<List<PromotionPostResponse>> getAllPosts() {
        List<PromotionPostResponse> posts = promotionPostService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        try {
            PromotionPostResponse post = promotionPostService.getPostById(id);
            return ResponseEntity.ok(post);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PromotionPostRequest request) {
        try {
            PromotionPostResponse created = promotionPostService.createPromotionPost(request);
            return ResponseEntity.created(URI.create("/api/promotions/" + created.getId())).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PromotionPostRequest request) {
        try {
            PromotionPostResponse updated = promotionPostService.updatePromotionPost(id, request);
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        try {
            promotionPostService.deletePromotionPost(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
