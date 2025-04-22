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
| PostView       | 게시글 조회 기록                      |

---

# ✅ 2. 논리적 ERD 설계

## 📌 User (회원)

| 필드명     | 타입        | 설명                         |
|------------|----------------------|------------------------------|
| id         | BIGINT (PK)          | 사용자 고유 ID               |
| username   | VARCHAR(20) UNIQUE     | 로컬 로그인 ID (중복 불가)         |
| password   | VARCHAR(255)      | 비밀번호 (암호화)            |
| email      | VARCHAR(100) NOT NULL, UNIQUE | 이메일 주소 (중복 불가)
| nickname   | VARCHAR(10) NOT NULL, UNIQUE     | 닉네임 (NULL X, 중복 불가)                       |
| provider | VARCHAR(20) NOT NULL, DEFAULT 'local' | 로그인 제공자(디폴트: local) (kakao, google 등) |
| social_id  | VARCHAR(50) UNIQUE(social_id, provider) | 소셜 서비스에서의 고유 ID |
| role       | ENUM('USER', 'ADMIN') NOT NULL, DEFAULT 'USER' | USER / ADMIN (디폴트: USER)     |
| created_at | DATETIME NOT NULL    | 가입일                       |
| updated_at | DATETIME             | 수정일                       |
| is_deleted | BOOLEAN DEFAULT FALSE | Soft Delete                  |
| deleted_at | DATETIME             | 탈퇴일                       |
>💡 `ENUM` 타입은 프론트에서 한글로 매핑!
>
>💡 제약조건: `UNIQUE(social_id, provider)` | 같은 `provider`에서 동일한 `social_id`가 두 번 들어오면 안 된다.
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
| status      | ENUM('SCHEDULED', 'ONGOING', 'COMPLETED') | 예정 / 진행중 / 종료          |
| created_at  | DATETIME NOT NULL     | 생성일                        |
| updated_at  | DATETIME     | 수정일                        |
| is_deleted | BOOLEAN DEFAULT FALSE      | Soft Delete                   |

---

## 📌 Location (지도 위치 정보)

| 필드명   | 타입         | 설명                          |
|----------|--------------|-------------------------------|
| id       | BIGINT (PK)  | 장소 ID                       |
| name     | VARCHAR(100) NOT NULL | 장소 이름 (예: 홍대 걷고 싶은 거리) |
| latitude | DOUBLE       | 위도                          |
| longitude| DOUBLE       | 경도                          |
| region   | VARCHAR(50) NOT NULL or ENUM NOT NULL | 지역명 (ex. 서울)          |
| description | TEXT        | 장소 설명                   |
| is_active | BOOLEAN DEFAULT TRUE     | 장소 사용 여부 (기본 True)    |

---

## 📌 PromotionPost (버스커 홍보 게시글)

| 필드명     | 타입                      | 설명                          |
|------------|---------------------------|-------------------------------|
| id         | BIGINT (PK)               | 게시글 ID                    |
| uuid       | CHAR(36) NOT NULL, UNIQUE | 외부 공개용 식별자           |
| user_id    | BIGINT NOT NULL (FK)      | 작성자 ID (User.id)          |
| location_id | BIGINT NOT NULL (FK)     | 장소 ID (Location.id)         |
| title      | VARCHAR(100) NOT NULL     | 제목                         |
| content    | TEXT NOT NULL             | 내용                         |
| media_url  | TEXT                      | 사진/영상 URL                |
| view_count | INT DEFAULT 0             | 게시글 조회 수               |
| created_at | DATETIME NOT NULL         | 작성일                       |
| updated_at | DATETIME                  | 수정일                        |
| is_deleted | BOOLEAN DEFAULT FALSE     | Soft Delete                  |
| category   | ENUM('ART', 'DANCE', 'MUSIC', 'TALK') NOT NULL | 게시글 분류(예술, 댄스, 음악, 토크) | 

---

## 📌 Review (홍보 게시글 리뷰)

| 필드명     | 타입                             | 설명                                       |
|------------|-----------------------------------|--------------------------------------------|
| id         | BIGINT (PK)                       | 리뷰 ID                                    |
| post_id    | BIGINT NOT NULL (FK)              | 대상 게시글 ID (PromotionPost.id) |
| user_id    | BIGINT NOT NULL (FK)              | 작성자 ID                         |
| rating     | INT CHECK (rating BETWEEN 1 AND 5)    | 별점                  |
| comment    | TEXT                              | 리뷰 내용                                  |
| created_at | DATETIME NOT NULL                 | 작성일                            |
| updated_at | DATETIME                          | 수정일                                     | 
| is_deleted | BOOLEAN DEFAULT FALSE             | Soft Delete                   |

>💡 제약조건: `UNIQUE(post_id, user_id)` | 하나의 게시글에 한 유저당 리뷰 하나만 작성 가능.

---

## 📌 BoardPost (자유 게시판)

| 필드명     | 타입                         | 설명                          |
|------------|------------------------------|-------------------------------|
| id         | BIGINT (PK)                  | 게시글 ID                    |
| uuid       | CHAR(36) NOT NULL, UNIQUE    | 외부 공개용 식별자           |
| user_id    | BIGINT NOT NULL (FK)         | 작성자 ID                    |
| title      | VARCHAR(100) NOT NULL        | 제목                         |
| content    | TEXT NOT NULL                | 내용                         |
| view_count | INT DEFAULT 0                | 게시글 조회 수               |
| created_at | DATETIME NOT NULL            | 작성일                       |
| updated_at | DATETIME                     | 수정일                       |
| is_deleted | BOOLEAN DEFAULT FALSE        | Soft Delete                   |

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
| parent_id  | BIGINT (FK - Comment.id, NULL 허용) | 부모 댓글 ID (대  댓글용)   |

---

## 📌 PostView (게시글 조회 기록)

| 필드명     | 타입                        | 설명                            |
|------------|-----------------------------|---------------------------------|
| id         | BIGINT (PK)                 | 고유 ID                         |
| post_id    | BIGINT NOT NULL             | 게시글 ID (FK - 홍보 or 자유)  |
| post_type  | ENUM('PROMOTION', 'BOARD') NOT NULL | 게시판 종류 구분               |
| user_id    | BIGINT NOT NULL             | 조회한 사용자 ID (FK)          |
| viewed_at  | DATETIME NOT NULL           | 조회 시간                      |

>💡 제약조건: `UNIQUE(post_id, post_type, user_id)` | 동일 사용자는 한 게시글에 대해 단 1회만 조회 수가 증가한다.

---

## 🔄 관계 정리

- User (1) ↔ (N) Busking  
- User (1) ↔ (N) PromotionPost  
- User (1) ↔ (N) Review
- User (1) ↔ (N) Comment
- PromotionPost (1) ↔ (N) Review  
- Busking (N) ↔ (1) Location
- BoardPost (1) ↔ (N) Comment 
