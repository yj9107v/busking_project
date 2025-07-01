# 🧪 PromotionPost 도메인 테스트 문서

> 버스킹 플랫폼 프로젝트의 `promotion` 도메인에 대한 단위 테스트, 통합 테스트, 예외 테스트까지 전체 테스트 흐름을 문서화한 내용입니다.

---

## ✅ 테스트 목표

`PromotionPost` 도메인의 핵심 기능과 시나리오를 다음과 같이 검증합니다:

1. 게시글 생성
2. 게시글 수정
3. 게시글 조회
4. 게시글 삭제 (소프트 삭제)
5. 유효성 검증
6. 예외 처리
7. 전체 흐름 테스트 (E2E)

---

## 📁 테스트 파일 구성

| 파일 경로 | 역할 |
|-----------|------|
| `PromotionPostServiceTest.java` | 서비스 계층 단위 테스트 |
| `PromotionPostRepositoryTest.java` | JPA 저장/조회 테스트 |
| `PromotionPostControllerTest.java` | API(MockMvc) 테스트 |
| `PromotionPostFlowIntegrationTest.java` | 전체 플로우 통합 테스트 (E2E) |

---

## 🧪 테스트 시나리오 정리

### 1️⃣ 생성 테스트

- **대상:** `Service.createPromotionPost()`
- **검증:** 응답 DTO의 title, category, place 등이 입력값과 일치하는지 확인

### 2️⃣ 수정 테스트

- **대상:** `Service.updatePromotionPost(id, dto)`
- **검증:** 수정된 필드(title, content, category 등)가 반영되었는지 확인

### 3️⃣ 삭제 테스트

- **대상:** `Service.deletePromotionPost(id)`
- **검증:** `isDeleted = true`로 처리되고, 이후 조회 시 예외 발생하는지 확인

### 4️⃣ 조회 실패 테스트

- **대상:** 존재하지 않는 ID로 `getPostById(id)`
- **검증:** `NoSuchElementException` 발생 및 메시지 검증

### 5️⃣ 유효성 검증 실패 테스트

- **대상:** `POST /api/promotions`
- **검증 항목:**
    - `title`이 공백 → 400 Bad Request
    - `category`가 enum(`ART`, `MUSIC`, `DANCE`, `TALK`) 외 → 400 Bad Request

### 6️⃣ 통합 흐름 테스트 (E2E)

- **파일:** `PromotionPostFlowIntegrationTest.java`
- **검증 흐름:**
    1. 게시글 생성 → Location 헤더에서 ID 추출
    2. 게시글 수정 → PUT 요청
    3. 수정된 게시글 조회 → GET 요청
    4. 게시글 삭제 → DELETE 요청
    5. 삭제된 게시글 재조회 → 404 응답

---

## 🛠️ 사용된 기술 및 설정

- `@SpringBootTest`, `@Transactional`, `@WithMockUser`
- `MockMvc`를 통한 Controller 통합 테스트
- `TestDataFactory`를 이용한 테스트 데이터 일관성 유지
- 예외 메시지 검증: `assertThatThrownBy(...).hasMessageContaining(...)`

---

## ✅ 테스트 실행 명령어

```bash
./gradlew -Dspring.profiles.active=test clean test
