package busking.busking_project.comment;

import busking.busking_project.comment.dto.CommentRequestDto;
import busking.busking_project.comment.dto.CommentResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 등록
    @PostMapping
    public ResponseEntity<CommentResponseDto> create(@Valid @RequestBody CommentRequestDto dto) {
        return ResponseEntity.ok(commentService.create(dto));
    }

    // 게시글 기준 댓글 전체 조회 (Soft Delete 제외)
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getByPost(@RequestParam Long postId) {
        return ResponseEntity.ok(commentService.getByPost(postId));
    }

    // 댓글 수정
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> update(@PathVariable Long id, @Valid @RequestBody CommentRequestDto dto) {
        return ResponseEntity.ok(commentService.update(id, dto));
    }

    // 댓글 논리 삭제(Soft Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commentService.delete(id); // 내부에서 isDeleted만 true로!
        return ResponseEntity.noContent().build();
    }
}