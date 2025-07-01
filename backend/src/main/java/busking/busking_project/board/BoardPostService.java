package busking.busking_project.board;

import busking.busking_project.board.dto.BoardPostRequestDto;
import busking.busking_project.board.dto.BoardPostResponseDto;
import busking.busking_project.board.BoardPostRepository;
import busking.busking_project.user.User;
import busking.busking_project.user.UserRepository;
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
    private final UserRepository userRepository;

    // Soft Delete가 적용된 게시글 전체 조회
    public List<BoardPostResponseDto> getAllPosts() {
        return boardPostRepository.findAllByIsDeletedFalse().stream()
                .map(BoardPostResponseDto::new)
                .collect(Collectors.toList());
    }

    // Soft Delete가 적용된 게시글 단건 조회
    public BoardPostResponseDto getPostById(Long id) {
        BoardPost post = boardPostRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 게시글입니다."));
        return new BoardPostResponseDto(post);
    }

    // 게시글 생성
    @Transactional
    public BoardPostResponseDto createPost(BoardPostRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("작성자(사용자)를 찾을 수 없습니다."));

        BoardPost post = BoardPost.builder()
                .uuid(UUID.randomUUID().toString())
                .user(user) // 객체 연관관계로 저장
                .title(dto.getTitle())
                .content(dto.getContent())
                .viewCount(0)
                .isDeleted(false)
                .build();

        return new BoardPostResponseDto(boardPostRepository.save(post));
    }

    // 게시글 수정
    @Transactional
    public BoardPostResponseDto updatePost(Long id, BoardPostRequestDto dto) {
        BoardPost post = boardPostRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NoSuchElementException("수정할 게시글이 없습니다."));

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        // 작성자(user)는 변경하지 않음

        return new BoardPostResponseDto(boardPostRepository.save(post));
    }

    // 게시글 논리 삭제(Soft Delete)
    @Transactional
    public void deletePost(Long id) {
        BoardPost post = boardPostRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NoSuchElementException("삭제할 게시글이 없습니다."));
        post.setDeleted(true); // setIsDeleted(X) → setDeleted(O)
        boardPostRepository.save(post);
    }
}
