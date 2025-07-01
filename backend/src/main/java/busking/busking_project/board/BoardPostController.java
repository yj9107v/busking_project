package busking.busking_project.board;

import busking.busking_project.board.dto.BoardPostRequestDto;
import busking.busking_project.board.dto.BoardPostResponseDto;
import busking.busking_project.board.BoardPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardPostController {

    private final BoardPostService boardPostService;

    // 전체 게시글 조회 (Soft Delete 적용)
    @GetMapping
    public ResponseEntity<List<BoardPostResponseDto>> getAll() {
        return ResponseEntity.ok(boardPostService.getAllPosts());
    }

    // 단건 게시글 조회 (Soft Delete 적용)
    @GetMapping("/{id}")
    public ResponseEntity<BoardPostResponseDto> getPost(@PathVariable Long id) {
        BoardPostResponseDto post = boardPostService.getPostById(id);
        if (post == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(post);
    }

    // 게시글 등록
    @PostMapping
    public ResponseEntity<BoardPostResponseDto> create(@RequestBody BoardPostRequestDto dto) {
        BoardPostResponseDto created = boardPostService.createPost(dto);
        return ResponseEntity.created(URI.create("/api/boards/" + created.getId())).body(created);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<BoardPostResponseDto> update(@PathVariable Long id, @RequestBody BoardPostRequestDto dto) {
        BoardPostResponseDto updated = boardPostService.updatePost(id, dto);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    // 게시글 논리 삭제(Soft Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boardPostService.deletePost(id); // 내부에서 isDeleted만 true로
        return ResponseEntity.noContent().build();
    }
}
