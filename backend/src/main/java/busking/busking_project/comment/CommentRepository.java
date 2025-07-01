import busking.busking_project.board.BoardPost;
import busking.busking_project.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // BoardPost 객체 기준(엔티티 연관관계)
    List<Comment> findByPostIdAndIsDeletedFalseOrderByCreatedAtAsc(BoardPost postId);
}
