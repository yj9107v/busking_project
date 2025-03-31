# ✅ 1. 주요 테이블(엔티티) 목록

| 테이블명        | 설명                                 |
|----------------|--------------------------------------|
| User           | 회원 정보 (버스커, 일반 사용자 구분 포함) |
| Busking        | 버스킹 일정 및 상태                     |
| Location       | 버스킹 장소 (지도 위치 정보)              |
| PromotionPost  | 버스커의 자기 홍보 게시물                 |
| Review         | 홍보 게시글에 남기는 리뷰                 |
| BoardPost      | 자유 게시판 글                          |
| Comment        | 자유 게시판 댓글                      |

---

# ✅ 2. 기본 ERD 설계 초안

## 📌 User (회원)

| 필드명     | 타입        | 설명                         |
|------------|-------------|------------------------------|
| id         | BIGINT (PK) | 사용자 고유 ID               |
| username   | VARCHAR     | 로그인 ID                    |
| password   | VARCHAR     | 비밀번호 (암호화)            |
| nickname   | VARCHAR     | 닉네임                       |
| role       | ENUM        | USER / BUSKER / ADMIN 등     |
| created_at | DATETIME    | 가입일                       |
| updated_at | DATETIME    | 수정일                       |
| is_deleted | BOOLEAN DEFAULT FALSE    | Soft Delete                  |
| deleted_at | DATETIME    | 탈퇴일                       |

---

## 📌 Busking (버스킹 일정)

| 필드명      | 타입         | 설명                          |
|-------------|--------------|-------------------------------|
| id          | BIGINT (PK)  | 일정 ID                       |
| uuid        | CHAR(36) NOT NULL, UNIQUE | 외부 공개용 식별자            |
| user_id     | BIGINT NOT NULL (FK)  | 버스커 ID (User.id)           |
| location_id | BIGINT NOT NULL (FK)  | 장소 ID (Location.id)         |
| date        | DATE         | 날짜                          |
| start_time  | TIME         | 시작 시간                     |
| end_time    | TIME         | 종료 시간                     |
| description | VARCHAR(100) | 공연 소개 문구                |
| status      | ENUM         | 예정 / 진행중 / 종료          |
| created_at  | DATETIME     | 생성일                        |
| updated_at  | DATETIME     | 수정일                        |
| is_deleted | BOOLEAN DEFAULT FALSE      | Soft Delete                   |

---

## 📌 Location (지도 위치 정보)

| 필드명   | 타입         | 설명                          |
|----------|--------------|-------------------------------|
| id       | BIGINT (PK)  | 장소 ID                       |
| name     | VARCHAR(100) | 장소 이름 (예: 홍대 걷고 싶은 거리) |
| latitude | DOUBLE       | 위도                          |
| longitude| DOUBLE       | 경도                          |
| region   | VARCHAR(50) or ENUM | 지역명 (ex. 서울)          |
| description | TEXT        | 장소 설명                   |
| is_active | Boolean     | 장소 사용 여부 (기본 True)    |

---

## 📌 PromotionPost (버스커 홍보 게시글)

| 필드명     | 타입                      | 설명                          |
|------------|---------------------------|-------------------------------|
| id         | BIGINT (PK)               | 게시글 ID                    |
| uuid       | CHAR(36) NOT NULL, UNIQUE | 외부 공개용 식별자           |
| user_id    | BIGINT NOT NULL (FK)      | 작성자 ID (User.id)          |
| title      | VARCHAR(100) NOT NULL     | 제목                         |
| content    | TEXT NOT NULL             | 내용                         |
| media_url  | TEXT                      | 사진/영상 URL                |
| created_at | DATETIME NOT NULL         | 작성일                       |
| updated_at | DATETIME                  | 수정일                        |
| is_deleted | BOOLEAN DEFAULT FALSE     | Soft Delete                  |

---

## 📌 Review (홍보 게시글 리뷰)

| 필드명     | 타입         | 설명                             |
|------------|--------------|----------------------------------|
| id         | BIGINT (PK)  | 리뷰 ID                          |
| post_id    | BIGINT NOT NULL, UNIQUE (FK)  | 대상 게시글 ID (PromotionPost.id) |
| user_id    | BIGINT NULL, UNIQUE (FK)  | 작성자 ID                        |
| rating     | INT CHECK (1~5)    | 별점                             |
| comment    | TEXT         | 리뷰 내용                        |
| created_at | DATETIME     | 작성일                           |
| updated_at | DATETIME     | 수정일                           |
| is_deleted | BOOLEAN DEFAULT FALSE     | Soft Delete                      |

---

## 📌 BoardPost (자유 게시판)

| 필드명     | 타입         | 설명                          |
|------------|--------------|-------------------------------|
| id         | BIGINT (PK)  | 게시글 ID                    |
| uuid       | CHAR(36) NOT NULL, UNIQUE    | 외부 공개용 식별자           |
| user_id    | BIGINT NOT NULL (FK)  | 작성자 ID                    |
| title      | VARCHAR(100) NOT NULL      | 제목                         |
| content    | TEXT NOT NULL        | 내용                         |
| created_at | DATETIME     | 작성일                       |
| updated_at | DATETIME     | 수정일                       |
| is_deleted | BOOLEAN DEFAULT FALSE    | Soft Delete                   |

---

## 📌 Comment (자유 게시판 댓글)

| 필드명     | 타입                           | 설명                                |
|------------|--------------------------------|-------------------------------------|
| id         | BIGINT (PK)                    | 댓글 ID                             |
| post_id    | BIGINT NOT NULL (FK)           | 대상 게시글 ID (BoardPost.id)      |
| user_id    | BIGINT NOT NULL (FK)           | 작성자 ID (User.id)                 |
| content    | TEXT NOT NULL                  | 댓글 내용                            |
| created_at | DATETIME NOT NULL              | 작성일                               |
| updated_at | DATETIME                       | 수정일                               |
| is_deleted | BOOLEAN DEFAULT FALSE          | Soft Delete                          |
| parent_id  | BIGINT (자기 참조, NULL 허용) | 대댓글인 경우 부모 댓글 ID (선택)   |

---

## 🔄 관계 정리

- User (1) ↔ (N) Busking  
- User (1) ↔ (N) PromotionPost  
- User (1) ↔ (N) Review
- User (1) ↔ (N) Comment
- PromotionPost (1) ↔ (N) Review  
- Busking (N) ↔ (1) Location
- BoardPost (N) ↔ (1) Comment 
