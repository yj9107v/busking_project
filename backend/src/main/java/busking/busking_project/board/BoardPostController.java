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

    @GetMapping
    public ResponseEntity<List<BoardPostResponseDto>> getAll() {
        return ResponseEntity.ok(boardPostService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardPostResponseDto> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(boardPostService.getPostById(id));
    }

    @PostMapping
    public ResponseEntity<BoardPostResponseDto> create(@RequestBody BoardPostRequestDto dto) {
        BoardPostResponseDto created = boardPostService.createPost(dto);
        return ResponseEntity.created(URI.create("/api/boards/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardPostResponseDto> update(@PathVariable Long id, @RequestBody BoardPostRequestDto dto) {
        return ResponseEntity.ok(boardPostService.updatePost(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boardPostService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
