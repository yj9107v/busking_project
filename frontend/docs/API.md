✅ 1단계: 회원 (회원가입, 로그인, 내 정보 조회)

📌 회원가입

항목	내용
Endpoint	POST /api/auth/signup
Method	POST
Request Body	
{
  "username": "busker123",
  "password": "secure1234",
  "nickname": "홍대버스커"
}
| Response |
{
  "message": "회원가입 성공",
  "userId": 1
}
| Auth | ❌ 필요 없음 |

📌 로그인 (JWT 발급)

항목	내용
Endpoint	POST /api/auth/login
Method	POST
Request Body	
{
  "username": "busker123",
  "password": "secure1234"
}
| Response |
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6...",
  "tokenType": "Bearer"
}
| Auth | ❌ 필요 없음 |

📌 내 정보 조회

항목	내용
Endpoint	GET /api/users/me
Method	GET
Headers	
Authorization: Bearer {JWT}
| Response |
{
  "id": 1,
  "username": "busker123",
  "nickname": "홍대버스커",
  "role": "BUSKER"
}
| Auth | ✅ 필요 |

✅ 2단계: 버스킹 일정 API

📌 버스킹 일정 등록 (버스커 전용)
항목	내용
Endpoint	POST /api/busking
Method	POST
Headers	
Authorization: Bearer {JWT}
| Request Body |
{
  "locationId": 3,
  "date": "2025-03-30",
  "startTime": "15:00",
  "endTime": "17:00"
}
| Response |
{
  "message": "버스킹 일정이 등록되었습니다.",
  "buskingId": 12
}
| Auth | ✅ 필요 (버스커 권한) |

📌 버스킹 일정 조회 (공개)
항목	내용
Endpoint	GET /api/busking?region=서울&date=2025-03-30
Method	GET
Query Param 예시	
region=서울
date=2025-03-30
| Response |
[
  {
    "id": 12,
    "buskerNickname": "홍대버스커",
    "location": "홍대 걷고 싶은 거리",
    "latitude": 37.5567,
    "longitude": 126.9236,
    "startTime": "15:00",
    "endTime": "17:00",
    "status": "예정"
  },
  ...
]
| Auth | ❌ 불필요 |

📌 버스킹 상태 변경 (버스커: 시작/종료 버튼)
항목	내용
Endpoint	PATCH /api/busking/{id}/status
Method	PATCH
Headers	
Authorization: Bearer {JWT}
| Request Body |
{
  "status": "진행중"  // 또는 "종료"
}
| Response |
{
  "message": "상태가 변경되었습니다.",
  "status": "진행중"
}
| Auth | ✅ 필요 (버스커 권한) |

📌 (선택) 버스킹 일정 삭제
항목	내용
Endpoint	DELETE /api/busking/{id}
Method	DELETE
Headers	
Authorization: Bearer {JWT}
| Response |
{
  "message": "버스킹 일정이 삭제되었습니다."
}
| Auth | ✅ 필요 (작성자만 가능) |

