package busking.busking_project.promotion;

import busking.busking_project.promotion.PromotionPost.Category;
import busking.busking_project.promotion.dto.PromotionPostRequest;
import busking.busking_project.promotion.dto.PromotionPostResponse;
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
public class PromotionPostService {

    private final PromotionPostRepository promotionPostRepository;
    private final UserRepository userRepository;

    /**
     * 전체 홍보 게시글 목록 조회 (삭제되지 않은 것만)
     */
    public List<PromotionPostResponse> getAllPosts() {
        // isDeleted == false인 게시글만 조회 후 DTO 변환
        return promotionPostRepository.findAllByIsDeletedFalse().stream()
                .map(PromotionPostResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 게시글 단건 조회 (삭제되지 않은 게시글만)
     */
    public PromotionPostResponse getPostById(Long id) {
        PromotionPost post = promotionPostRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 게시글입니다."));
        return new PromotionPostResponse(post);
    }

    /**
     * 홍보 게시글 생성
     * - 요청 DTO에서 userId(Long)를 받아서 User 객체로 변환
     * - 게시글 저장 후, 응답 DTO로 변환하여 반환
     */
    @Transactional
    public PromotionPostResponse createPromotionPost(PromotionPostRequest dto) {
        // userId(Long) → User 엔티티 조회 (없으면 예외 발생)
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        // PromotionPost 엔티티 생성
        PromotionPost post = PromotionPost.builder()
                .uuid(UUID.randomUUID().toString())
                .userId(user) // User 객체로 세팅
                .title(dto.getTitle())
                .content(dto.getContent())
                .mediaUrl(dto.getMediaUrl())
                .category(Category.valueOf(dto.getCategory()))
                .place(dto.getPlace())
                .viewCount(0)
                .isDeleted(false)
                .build();

        PromotionPost saved = promotionPostRepository.save(post);

        System.out.println("✅ 생성된 ID = " + saved.getId());

        return new PromotionPostResponse(saved);
    }

    /**
     * 홍보 게시글 수정
     * - 작성자 본인 또는 관리자만 수정 가능
     * - userId(Long)로 User 객체 조회, 권한 검증 후 수정
     * @param currentUserId   : 현재 로그인 사용자 id
     * @param currentUserRole : 현재 로그인 사용자 role(예: "ADMIN" 또는 "USER")
     */
    @Transactional
    public PromotionPostResponse updatePromotionPost(
            Long id,
            PromotionPostRequest dto,
            Long currentUserId,
            String currentUserRole
    ) {
        // 기존 게시글 조회 (삭제된 글 제외)
        PromotionPost post = promotionPostRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NoSuchElementException("수정할 게시글을 찾을 수 없습니다."));

        // 권한 체크: 작성자이거나 관리자만 허용
        if (!post.getUserId().getId().equals(currentUserId) && !currentUserRole.equals("ADMIN")) {
            throw new SecurityException("작성자 또는 관리자만 수정할 수 있습니다.");
        }

        // 수정할 User 객체 조회
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        // PromotionPost 객체 재생성(Builder 사용)
        PromotionPost updated = PromotionPost.builder()
                .id(post.getId())
                .uuid(post.getUuid())
                .userId(user)
                .title(dto.getTitle())
                .content(dto.getContent())
                .mediaUrl(dto.getMediaUrl())
                .category(Category.valueOf(dto.getCategory()))
                .place(dto.getPlace())
                .viewCount(post.getViewCount())
                .isDeleted(false)
                .build();

        return new PromotionPostResponse(promotionPostRepository.save(updated));
    }

    /**
     * 홍보 게시글 삭제 (Soft delete)
     * - 작성자 본인 또는 관리자만 삭제 가능
     * @param currentUserId   : 현재 로그인 사용자 id
     * @param currentUserRole : 현재 로그인 사용자 role(예: "ADMIN" 또는 "USER")
     */
    @Transactional
    public void deletePromotionPost(Long id, Long currentUserId, String currentUserRole) {
        PromotionPost post = promotionPostRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 게시글입니다."));

        // 권한 체크: 작성자이거나 관리자만 허용
        if (!post.getUserId().getId().equals(currentUserId) && !currentUserRole.equals("ADMIN")) {
            throw new SecurityException("작성자 또는 관리자만 삭제할 수 있습니다.");
        }
        post.setIsDeleted(true);
        promotionPostRepository.save(post);
    }
}
