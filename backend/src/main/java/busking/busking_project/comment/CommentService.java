package busking.busking_project.comment;

import busking.busking_project.board.BoardPost;
import busking.busking_project.board.BoardPostRepository;
import busking.busking_project.comment.dto.CommentRequestDto;
import busking.busking_project.comment.dto.CommentResponseDto;
import busking.busking_project.user.User;
import busking.busking_project.user.UserRepository;
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
    private final BoardPostRepository boardPostRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponseDto create(CommentRequestDto dto) {
        BoardPost post = boardPostRepository.findById(dto.getPostId())
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        Comment parent = null;
        if (dto.getParentId() != null) {
            parent = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new NoSuchElementException("부모 댓글을 찾을 수 없습니다."));
        }

        Comment comment = Comment.builder()
                .postId(post)
                .user(user)
                .content(dto.getContent())
                .parent(parent)
                .isDeleted(false)
                .build();

        return new CommentResponseDto(commentRepository.save(comment));
    }

    public List<CommentResponseDto> getByPost(Long postId) {
        BoardPost post = boardPostRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));
        return commentRepository.findByPostIdAndIsDeletedFalseOrderByCreatedAtAsc(post).stream()
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
        comment.setDeleted(true);
        commentRepository.save(comment);
    }
}
