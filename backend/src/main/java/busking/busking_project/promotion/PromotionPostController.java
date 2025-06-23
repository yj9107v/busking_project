package busking.busking_project.promotion;

import busking.busking_project.promotion.dto.PromotionPostRequest;
import busking.busking_project.promotion.dto.PromotionPostResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/promotions")
public class PromotionPostController {

    private final PromotionPostService promotionPostService;

    /**
     * 전체 게시글 조회
     */
    @GetMapping
    public ResponseEntity<List<PromotionPostResponse>> getAllPosts() {
        List<PromotionPostResponse> posts = promotionPostService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    /**
     * 게시글 단건 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        try {
            PromotionPostResponse post = promotionPostService.getPostById(id);
            return ResponseEntity.ok(post);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 게시글 생성
     */
    @PostMapping
    public ResponseEntity<?> createPost(
            @Valid @RequestBody PromotionPostRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        try {
            PromotionPostResponse created = promotionPostService.createPromotionPost(request);
            return ResponseEntity.created(URI.create("/api/promotions/" + created.getId())).body(created);
        } catch (IllegalArgumentException | NoSuchElementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 게시글 수정
     * - userId, role은 프론트에서 함께 보내주거나, 인증 구현 후에는 SecurityContext 등에서 추출해야 함
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PromotionPostRequest request,
            @RequestParam("userId") Long currentUserId,
            @RequestParam("role") String currentUserRole
    ) {
        try {
            PromotionPostResponse updated = promotionPostService.updatePromotionPost(
                    id, request, currentUserId, currentUserRole
            );
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    /**
     * 게시글 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(
            @PathVariable Long id,
            @RequestParam("userId") Long currentUserId,
            @RequestParam("role") String currentUserRole
    ) {
        try {
            promotionPostService.deletePromotionPost(id, currentUserId, currentUserRole);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
}
