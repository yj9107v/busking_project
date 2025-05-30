# 🔐 로컬 회원가입 API 명세서
> 작성 날짜: 2025/04/18

사용자가 아이디, 이메일, 비밀번호, 닉네임을 입력하여 로컬 회원 계정을 생성하고,
서버로부터 가입 성공 여부 및 사용자 ID를 응답받는 `API`입니다.  

---

## ✅ API 개요

| 항목 | 내용                               |
|------|----------------------------------|
| API 이름 | 회원가입 API                         |
| 요청 URL | `/api/users`              |
| HTTP 메서드 | `POST`                           |
| 요청 헤더 | `Content-Type: application/json` |

---

## ✅ 요청 예시 (Request)

```http
POST /api/users HTTP/1.1
Content-Type: application/json
```

```json
{
  "username": "busking123",
  "password": "securepassword11@",
  "email": "busking@google.com",
  "nickname": "홍길동"
}
```

### 🔎 요청 설명
> #### ✅ start-line
- HTTP 메서드: `POST`
- 요청 대상: `/api/users`
- HTTP 버전: `HTTP/1.1`

> #### ✅ 요청 header
- `Content-Type: application/json`

> #### ✅ message body
| 필드명 | 타입     | 제약 조건                                      | 설명           |
|-------|--------|--------------------------------------------|--------------|
| username | string | `5~20자`, `영문 + 숫자 + 밑줄(_)`만 허용, `중복 불가`(고유) | 로그인용 ID      | 
| password | string | `8~20자`, `영문 + 숫자 + 특수문자`만 허용                  | 로그인 비밀번호     |
| email | string | `100자 이하`, `이메일 형식`만 허용, `중복 불가`(고유)       | 알림 및 인증용 이메일 |
| nickname | string | `2~30자`, `한글, 영문, 숫자`만 허용, `중복 불가`(고유)     | 사용자 표시 이름    |

---

## ✅ 응답 예시 (Responses)

### ✅ 회원가입 성공 (201 Created)

```http
HTTP/1.1 201 Created
Content-Type: application/json
Location: /api/users/1
```

```json
{
 "success": true,
 "message": "회원가입이 성공적으로 완료되었습니다.",
 "data": {
  "id": 1,
  "username": "busking123",
  "nickname": "버스킹짱",
  "created_at": "2025-04-06T16:00:00"
 }
}
```

### 🔐 성공 응답 설명

> #### ✅ start-line
- HTTP 버전: `HTTP/1.1`
- HTTP 상태코드: `201 Created`

> #### ✅ 응답 header
- `Content-Type: application/json` 
- `Location: api/users/1`

> #### ✅message body
| 필드명              | 타입                | 설명                         |
|------------------|-------------------|----------------------------|
| `success`        | boolean           | 요청 처리 성공 여부 (`true`로 고정됨)  |
| `message`        | string            | 사용자에게 보여줄 성공 메세지           |
| `data.id`        | number            | 생성된 사용자 고유 ID              |
| `data.username`  | string            | 사용자가 입력한 아이디               |
| `data.nickname`  | string            | 사용자가 입력한 닉네임               |
| `data.created_at` | string (datetime) | 회원가입 처리 완료 시간(ISO 8601 형식) |

---

## ❌ 실패 응답 예시

### 🔐 실패 응답 명세
| 케이스           | message                                     | errorCode                      |
|---------------|---------------------------------------------|--------------------------------|
| `username` 중복 | 이미 사용 중인 아이디입니다.                            | `USER_USERNAME_DUPLICATED`     |
| `username` 형식 | 아이디는 5~20자, 영문 + 숫자 + 밑줄(_)만 허용됩니다.         | `USER_USERNAME_INVALID_FORMAT` |
| `password` 형식 | 비밀번호는 8~20자, 영문 + 숫자 + 특수문자만 허용됩니다.         | `USER_PASSWORD_INVALID_FORMAT` |
| `email` 중복    | 이미 사용 중인 이메일입니다. (동일한 이메일 로컬 or 소셜 회원가입 완료) | `USER_EMAIL_DUPLICATED`        |
| `email` 형식    | 유효한 이메일 형식이 아닙니다. `ex) example@google.com`  | `USER_EMAIL_INVALID_FORMAT`    |
| `nickname` 중복 | 이미 사용 중인 닉네임입니다.                            | `USER_NICKNAME_DUPLICATED`     |
| `nickname` 형식 | 닉네임은 2~30자, 한글, 영문, 숫자만 허용됩니다.              | `USER_NICKNAME_INVALID_FORMAT` |


### 🚫 400 Bad Request (중복 시)

```http
HTTP/1.1 400 Bad Request
Content-Type: application/json
```

```json
{
 "success": false,
 "message": "이미 사용 중인 아이디입니다.",
 "errorCode": "USER_USERNAME_DUPLICATED",
 "data": null
}
```

### 🔐 실패 응답 설명
> #### ✅ start-line
- HTTP 버전: `HTTP/1.1`
- HTTP 상태코드: `400 Bad Request`

> #### ✅ 응답 header
- `Content-Type: application/json`

> #### ✅message body

| 필드명        | 타입      | 설명                                          |
|------------|---------|---------------------------------------------|
| `success`  | boolean | 요청 실패이므로 항상 `false`                         |
| `message`  | string  | 사용자에게 표시할 수 있는 에러 메세지                       |
| `errorCode` | string  | 클라이언트/프론트가 조건 분기나 다국어 처리를 위해 사용할 수 있는 에러 코드 |
| `data` | null | 실패 상황이므로 항상 `null` 반환 |

---

### 🚫 400 Bad Request (형식 오류 시)

```http
HTTP/1.1 400 Bad Request
Content-Type: application/json
```

```json
{
 "success": false,
 "message": "유효한 이메일 형식이 아닙니다. ex) example@google.com",
 "errorCode": "USER_EMAIL_INVALID_FORMAT",
 "data": null
}
```

---

## 📌 비고

- 회원가입 성공 시, 신규 사용자 계정이 DB에 저장되며, 기본적으로 `ROLE_USER` 권한이 부여됩니다.
- 동일한 `username`, `email`, `nickname`에 대해서는 중복 가입이 불가하며, 각각에 대한 검증 로직이 포함되어 있습니다.
- 응답은 `success`, `message`, `data` 필드를 포함한 통일된 구조를 사용하며, 실패 시에는 `errorCode`로 실패 원인을 분기할 수 있습니다.
- 이 `API`는 소셜 로그인이 아닌 `로컬 회원가입` 전용이며, 로그인 방식 구분을 위해 `provider = 'local'`로 저장됩니다.
- 모든 필드는 `프론트엔드`와 `백엔드`에서 동일한 `유효성 검증 규칙`을 따르며, `서버` 측 검증은 필수로 수행됩니다.

