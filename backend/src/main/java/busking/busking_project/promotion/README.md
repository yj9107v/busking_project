
# 📚 Spring Boot 계층 구조 및 PromotionPost 흐름 정리

이 문서는 Spring Boot 기반 프로젝트에서 사용되는 주요 계층 (`Entity`, `DTO`, `Repository`, `Service`, `Controller`)의 역할을 설명하고, 실제 예제인 `PromotionPost` 기능의 전체 흐름을 구조적으로 정리한 문서입니다.

---

## 🧱 전체 구조 (PromotionPost 기준)

```
📁 promotion/
├── PromotionPost.java              # Entity (DB 테이블 구조)
├── PromotionPostRepository.java   # Repository (DB 접근)
├── PromotionPostService.java      # Service (비즈니스 로직)
├── PromotionPostController.java   # Controller (API 요청 처리)
└── dto/
    ├── PromotionPostRequest.java  # DTO - 요청
    └── PromotionPostResponse.java # DTO - 응답
```

---

## ✅ 1. Entity - `PromotionPost.java`

> 데이터베이스 테이블과 직접 매핑되는 클래스

```java
@Entity
@Table(name = "promotion_post")
public class PromotionPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;
    private Long userId;
    private String title;
    private String content;
    private String mediaUrl;
    private Category category;
    private Integer viewCount;
    private Boolean isDeleted;
    private String place;
}
```

- `@Entity`를 통해 JPA가 이 클래스를 테이블로 인식
- 실제 DB에 저장되는 객체이며, 필드가 DB 컬럼에 대응됨

---

## 📂 2. Repository - `PromotionPostRepository.java`

> DB에 접근해 데이터를 CRUD하는 계층

```java
public interface PromotionPostRepository extends JpaRepository<PromotionPost, Long> {
    List<PromotionPost> findAllByIsDeletedFalse();
}
```

- `JpaRepository`를 상속하여 기본 CRUD 메서드를 자동 제공
- `findAllByIsDeletedFalse()` 같은 커스텀 메서드도 정의 가능

---

## 📦 3. DTO (Data Transfer Object)

> 외부에 노출되는 데이터를 전달하는 객체

### 📌 요청 DTO - `PromotionPostRequest.java`
```java
public class PromotionPostRequest {
    private String title;
    private String content;
    private String mediaUrl;
    private String place;
    private Category category;
}
```

### 📌 응답 DTO - `PromotionPostResponse.java`
```java
public class PromotionPostResponse {
    private Long id;
    private String title;
    private String content;

    public PromotionPostResponse(PromotionPost post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}
```

- Entity를 그대로 외부에 노출하지 않고, 필요한 필드만 추려서 사용
- 보안과 유연한 API 설계를 위해 사용됨

---

## ⚙️ 4. Service - `PromotionPostService.java`

> 핵심 비즈니스 로직을 담당하는 계층

```java
@Service
@RequiredArgsConstructor
public class PromotionPostService {

    private final PromotionPostRepository repository;

    public List<PromotionPostResponse> getAllPosts() {
        return repository.findAllByIsDeletedFalse().stream()
                .map(PromotionPostResponse::new)
                .collect(Collectors.toList());
    }

    public PromotionPostResponse getPostById(Long id) {
        PromotionPost post = repository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Post not found"));
        return new PromotionPostResponse(post);
    }

    public PromotionPost createPost(PromotionPostRequest request) {
        PromotionPost post = PromotionPost.builder()
            .uuid(UUID.randomUUID().toString())
            .title(request.getTitle())
            .content(request.getContent())
            .mediaUrl(request.getMediaUrl())
            .place(request.getPlace())
            .category(request.getCategory())
            .isDeleted(false)
            .viewCount(0)
            .build();
        return repository.save(post);
    }

    public void deletePost(Long id) {
        PromotionPost post = repository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Post not found"));
        post.setIsDeleted(true);
        repository.save(post);
    }
}
```

- 복잡한 로직, 트랜잭션 처리 등은 여기서 수행
- DB 접근은 Repository를 통해 이뤄짐

---

## 🌐 5. Controller - `PromotionPostController.java`

> 실제 HTTP 요청을 받아서 처리하는 계층

```java
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/promotions")
public class PromotionPostController {

    private final PromotionPostService service;

    @GetMapping
    public ResponseEntity<List<PromotionPostResponse>> getAllPosts() {
        return ResponseEntity.ok(service.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPostById(id));
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PromotionPostRequest request) {
        PromotionPost post = service.createPost(request);
        return ResponseEntity.created(URI.create("/api/promotions/" + post.getId())).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        service.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
```

- URL과 HTTP 메서드를 통해 클라이언트 요청을 처리
- 요청 데이터를 DTO로 받고, 응답도 DTO로 반환

---

## 🔁 전체 흐름 요약

```plaintext
[프론트엔드] → HTTP 요청 →
[Controller] → 서비스 호출 →
[Service] → DB 접근 →
[Repository] → Entity 조회 →
[Entity] → DTO로 변환 →
[Controller] → 응답 반환 →
[프론트엔드]
```

---

## 📌 정리

| 계층 | 설명 |
|------|------|
| Entity | DB 테이블 구조 정의 |
| Repository | DB 접근 로직 담당 |
| DTO | API 요청/응답 데이터 포맷 |
| Service | 실제 기능과 비즈니스 로직 수행 |
| Controller | 외부 요청 수신 및 응답 처리 |

---

## ✨ 추가 팁

- DTO는 Entity와 분리하여 API 유연성과 보안을 높입니다.
- Service는 로직을 Controller로부터 분리시켜 유지보수를 쉽게 합니다.
- Controller는 HTTP 요청만 담당하고 실제 로직은 Service에 위임합니다.
