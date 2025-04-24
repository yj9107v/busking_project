package com.busking.backend.controller;

import com.busking.backend.dto.comment.CommentRequestDto;
import com.busking.backend.dto.comment.CommentResponseDto;
import com.busking.backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> create(@RequestBody CommentRequestDto dto) {
        return ResponseEntity.ok(commentService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getByPost(@RequestParam Long postId) {
        return ResponseEntity.ok(commentService.getByPost(postId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> update(@PathVariable Long id, @RequestBody CommentRequestDto dto) {
        return ResponseEntity.ok(commentService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}