# 🔐 로컬 로그인 API 명세서
> 작성 날짜: 2025/04/19

사용자가 아이디와 비밀번호를 입력하여 인증하고, 서버로부터 JWT 토큰을 발급받는 `API`입니다.

---

## ✅ API 개요

| 항목 | 내용                               |
|------|----------------------------------|
| API 이름 | 로그인 API                          |
| 요청 URL | `/api/auth/login`                |
| HTTP 메서드 | `POST`                           |
| 요청 헤더 | `Content-Type: application/json` |

---

## ✅ 요청 예시

```http
POST /api/auth/login HTTP/1.1
Content-Type: application/json
```

```json
{
  "username": "busking123",
  "password": "securepassword@"
}
```

### 🔎 요청 설명
> #### ✅ start-line
- HTTP 메서드: `POST`
- 요청 대상: `/api/auth/login`
- HTTP 버전: `HTTP/1.1`

> #### ✅ 요청 header
- `Content-Type: application/json`

> #### ✅ message body

| 필드명 | 타입 | 설명          |
|--------|------|-------------|
| username | string | 로그인용 ID(고유) |
| password | string | 로그인 비밀번호    |

---

## ✅ 응답 예시

### ✅ 로그인 성공 (200 OK)

```http
HTTP/1.1 200 OK
Content-Type: application/json
```

```json
{
  "success": true,
  "message": "로그인이 성공적으로 완료되었습니다.",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
    "user": {
      "id": 1,
      "username": "busking123",
      "nickname": "버스킹짱"
    }
  }
}
```

### 🔐 성공 응답 설명

> #### ✅ start-line
- HTTP 버전: `HTTP/1.1`
- HTTP 상태코드: `200 OK`

> #### ✅ 응답 header
- `Content-Type: application/json`

> #### ✅message body
| 필드명                  | 타입                | 설명                                                 |
|----------------------|-------------------|----------------------------------------------------|
| `success`            | boolean           | 요청 처리 성공 여부 (`true`로 고정됨)                          |
| `message`            | string            | 사용자에게 보여줄 성공 메시지                                   |
| `data.accessToken`   | string | JWT 형식의 인증 토큰. 이후 인증이 필요한 요청에서 사용됨 (Bearer 토큰)     |
| `data.refreshToken`  | string | Access Token이 만료됐을 때, 새로운 Access Token을 발급받기 위한 토큰 |
| `data.user.id`       | number            | 로그인한 사용자 ID                                        |
| `data.user.username` | string            | 사용자 계정 ID                                          |
| `data.user.nickname` | string            | 사용자 닉네임                                            |

---

## ❌ 실패 응답 예시

### 🔐 실패 응답 명세
| 케이스           | message                                     | errorCode        |
|---------------|---------------------------------------------|------------------|
| 아이디, 비밀번호 불일치 |  아이디 또는 비밀번호가 잘못되었습니다. 아이디와 비밀번호를 정확히 입력해주세요.  | `USER_NOT_FOUND` |


### 🚫 401 Unauthorized (아이디, 비밀번호 불일치 시)

```http
HTTP/1.1 401 Unauthorized
Content-Type: application/json
```

```json
{
  "success": false,
  "message":  "아이디 또는 비밀번호가 잘못되었습니다. 아이디와 비밀번호를 정확히 입력해주세요.",
  "errorCode": "USER_NOT_FOUND",
  "data": null
}
```

---

## 📌 비고

- 로그인 성공 시 JWT 기반의 `accessToken`과 `refreshToken`이 발급됩니다.
- 이후 인증이 필요한 요청에는 다음과 같이 토큰을 포함해야 합니다:
  ```
  Authorization: Bearer <accessToken>
  ```
- 이 `API`는 로컬 로그인 전용이며, 소셜 로그인의 경우 별도 API 또는 provider 기반 분기가 필요합니다.
- `아이디` 및 `비밀번호` 일치 여부만 확인합니다.
- 로그인 응답의 `accessToken`은 일반 API 인증 요청 시, `refreshToken`은 토큰 만료 후 재발급 시 사용됩니다.
- `refreshToken`은 일반적으로 서버나 `DB`, 또는 `HttpOnly` 쿠키에 저장되어 보안성이 높게 관리됩니다.
- 토큰은 보통 `로컬스토리지` 또는 `세션스토리지`에 저장되며, 유효기간이 지나면 다시 로그인해야 합니다.
