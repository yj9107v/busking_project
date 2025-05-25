package busking.busking_project.board;

import busking.busking_project.board.BoardPost;
import busking.busking_project.board.dto.BoardPostResponseDto;
import busking.busking_project.board.dto.BoardPostRequestDto;
import busking.busking_project.board.BoardPostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardPostService {

    private final BoardPostRepository boardPostRepository;

    public List<BoardPostResponseDto> getAllPosts() {
        return boardPostRepository.findAllByIsDeletedFalse().stream()
                .map(BoardPostResponseDto::new)
                .collect(Collectors.toList());
    }

    public BoardPostResponseDto getPostById(Long id) {
        BoardPost post = boardPostRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 게시글입니다."));
        return new BoardPostResponseDto(post);
    }

    @Transactional
    public BoardPostResponseDto createPost(BoardPostRequestDto dto) {
        BoardPost post = BoardPost.builder()
                .uuid(UUID.randomUUID().toString())
                .userId(dto.getUserId()) // TODO: 로그인 유저로 대체
                .title(dto.getTitle())
                .content(dto.getContent())
                .viewCount(0)
                .isDeleted(false)
                .build();
        return new BoardPostResponseDto(boardPostRepository.save(post));
    }

    @Transactional
    public BoardPostResponseDto updatePost(Long id, BoardPostRequestDto dto) {
        BoardPost post = boardPostRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NoSuchElementException("수정할 게시글이 없습니다."));
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        return new BoardPostResponseDto(boardPostRepository.save(post));
    }

    @Transactional
    public void deletePost(Long id) {
        BoardPost post = boardPostRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NoSuchElementException("삭제할 게시글이 없습니다."));
        post.setIsDeleted(true);
        boardPostRepository.save(post);
    }
}
