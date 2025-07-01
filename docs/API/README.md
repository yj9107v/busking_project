# 📌 API 명세서

![API.png](../Image/API.png)

### 🔍 API 종류

#### ✅ 1. `User` → 회원과 관련된 모든 `API`
- 회원가입 - `/api/users`
- 로그인 - `/api/auth/login`
- 카카오, 구글 로그인 - `/api/auth/oatuh`
- 회원 정보 조회 - `/api/users/me`
- 닉네임 수정 - `/api/users/me/nickname`
- 비밀번호 변경 - `/api/users/me/password`
- 회원탈퇴 - `api/users/me`

---

#### ✅ 2. `PromotionPost` → 게시글과 관련된 모든 `API`
- 게시글 작성 - `/api/promotions`
- 게시글 수정 - `/api/promotions/{id}`
- 게시글 삭제 - `/api/promotions/{id}`

---

#### ✅ 3. `Review` → 리뷰와 관련된 모든 `API`
- 리뷰 작성 - `/api/promotions/{id}/reviewsRequestSyntax`
- 리뷰 수정 - `/api/promotions/{id}/reviews/{reviewId}`
- 리뷰 삭제 - `/api/promotions/{id}/reviews/{reviewId}`

---
