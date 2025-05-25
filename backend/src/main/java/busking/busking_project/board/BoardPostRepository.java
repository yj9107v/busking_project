package busking.busking_project.board;

import busking.busking_project.board.BoardPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardPostRepository extends JpaRepository<BoardPost, Long>{

    // 삭제되지 않은 전체 게시글
    List<BoardPost> findAllByIsDeletedFalse();

    // 단일 게시글 조회
    Optional<BoardPost> findByIdAndIsDeletedFalse(Long id);
}
