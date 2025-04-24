package com.busking.backend.service;

import com.busking.backend.domain.board.Comment;
import com.busking.backend.dto.comment.CommentRequestDto;
import com.busking.backend.dto.comment.CommentResponseDto;
import com.busking.backend.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponseDto create(CommentRequestDto dto) {
        Comment comment = Comment.builder()
                .postId(dto.getPostId())
                .userId(dto.getUserId())
                .content(dto.getContent())
                .parentId(dto.getParentId())
                .isDeleted(false)
                .build();

        return new CommentResponseDto(commentRepository.save(comment));
    }

    public List<CommentResponseDto> getByPost(Long postId) {
        return commentRepository.findByPostIdAndIsDeletedFalseOrderByCreatedAtAsc(postId).stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponseDto update(Long id, CommentRequestDto dto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("댓글을 찾을 수 없습니다."));
        comment.setContent(dto.getContent());
        return new CommentResponseDto(commentRepository.save(comment));
    }

    @Transactional
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("댓글을 찾을 수 없습니다."));
        comment.setIsDeleted(true);
        commentRepository.save(comment);
    }
}