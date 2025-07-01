package busking.busking_project.promotion;

import busking.busking_project.promotion.PromotionPost.Category;
import busking.busking_project.promotion.dto.PromotionPostRequest;
import busking.busking_project.promotion.dto.PromotionPostResponse;
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

    public List<PromotionPostResponse> getAllPosts() {
        return promotionPostRepository.findAllByIsDeletedFalse().stream()
                .map(PromotionPostResponse::new)
                .collect(Collectors.toList());
    }

    public PromotionPostResponse getPostById(Long id) {
        PromotionPost post = promotionPostRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 게시글입니다."));
        return new PromotionPostResponse(post);
    }

    @Transactional
    public PromotionPostResponse createPromotionPost(PromotionPostRequest dto) {
        PromotionPost post = PromotionPost.builder()
                .uuid(UUID.randomUUID().toString())
                .userId(1L) // TODO: 실제 로그인 사용자로 교체
                .title(dto.getTitle())
                .content(dto.getContent())
                .mediaUrl(dto.getMediaUrl())
                .category(Category.valueOf(dto.getCategory()))
                .place(dto.getPlace()) // ✅ place 추가
                .viewCount(0)
                .isDeleted(false)
                .build();

        PromotionPost saved = promotionPostRepository.save(post);

        System.out.println("✅ 생성된 ID = " + saved.getId()); // 디버깅 로그

        return new PromotionPostResponse(saved);
    }

    @Transactional
    public PromotionPostResponse updatePromotionPost(Long id, PromotionPostRequest dto) {
        PromotionPost post = promotionPostRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NoSuchElementException("수정할 게시글을 찾을 수 없습니다."));

        post = PromotionPost.builder()
                .id(post.getId())
                .uuid(post.getUuid())
                .userId(post.getUserId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .mediaUrl(dto.getMediaUrl())
                .category(Category.valueOf(dto.getCategory()))
                .place(dto.getPlace()) // ✅ place 반영
                .viewCount(post.getViewCount())
                .isDeleted(false)
                .build();

        return new PromotionPostResponse(promotionPostRepository.save(post));
    }

    @Transactional
    public void deletePromotionPost(Long id) {
        PromotionPost post = promotionPostRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 게시글입니다."));
        post.setIsDeleted(true);
        promotionPostRepository.save(post);
    }
}
