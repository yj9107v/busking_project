import React, { useState } from "react"; // ✅ React 및 상태 관리를 위한 useState 훅 import

// ✅ 로그인 폼 컴포넌트 정의
function LoginForm({ onLogin }) {
  const [username, setUsername] = useState("");   // 🆔 사용자 아이디 상태
  const [password, setPassword] = useState("");   // 🔐 사용자 비밀번호 상태
  const [error, setError] = useState("");         // ⚠ 에러 메시지 상태
  const [loading, setLoading] = useState(false);  // ⏳ 로그인 요청 중 로딩 상태

  // ✅ 일반 로그인 처리 함수
  const handleLocalLogin = async (e) => {
    e.preventDefault();           // 폼 기본 제출 동작 방지
    setError("");                 // 이전 에러 초기화
    setLoading(true);             // 로딩 상태 true 설정

    try {
      // 📨 로그인 API 요청 보내기
      const res = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include", // 세션 쿠키 포함
        body: JSON.stringify({ username, password }), // 사용자 입력값 전송
      });

      const result = await res.json(); // 응답 JSON 파싱

      if (res.ok) {
        console.log("✅ 로그인 성공 응답:", result);
        if (onLogin) onLogin(result); // 부모 컴포넌트로 로그인 결과 전달
      } else {
        setError(result.error || "로그인에 실패했습니다."); // 서버 응답 메시지 출력
      }
    } catch (err) {
      setError("서버 오류: " + err.message); // 네트워크 또는 서버 에러 처리
    } finally {
      setLoading(false); // 요청 완료 → 로딩 상태 해제
    }
  };

  // ✅ 소셜 로그인 요청 함수
  const handleSocialLogin = (provider) => {
    // 🔗 소셜 로그인 엔드포인트로 리다이렉트
    window.location.href = `http://localhost:8080/oauth2/authorization/${provider}`;
  };

  // ✅ 컴포넌트 렌더링
  return (
    <div
      style={{
        maxWidth: "400px",              // 최대 너비 제한
        margin: "0 auto",               // 가운데 정렬
        padding: "1rem",                // 안쪽 여백
        border: "1px solid #ccc",       // 테두리
        borderRadius: "8px"             // 둥근 테두리
      }}
    >
      <form onSubmit={handleLocalLogin}>
        <h3>🔐 일반 로그인</h3>

        {/* ❌ 로그인 실패 메시지 표시 */}
        {error && (
          <div style={{ color: "red", marginBottom: "10px" }}>
            {error}
          </div>
        )}

        {/* 🆔 아이디 입력 필드 */}
        <input
          type="text"
          placeholder="아이디"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
          style={{ marginBottom: "10px", width: "100%" }}
        />

        {/* 🔑 비밀번호 입력 필드 */}
        <input
          type="password"
          placeholder="비밀번호"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          style={{ marginBottom: "10px", width: "100%" }}
        />

        {/* 🔘 로그인 버튼 */}
        <button type="submit" disabled={loading} style={{ width: "100%" }}>
          {loading ? "로그인 중..." : "로그인"}
        </button>
      </form>

      {/* 🌐 소셜 로그인 영역 */}
      <div style={{ marginTop: "1.5rem" }}>
        <h4>🌐 소셜 로그인</h4>
        <button
          onClick={() => handleSocialLogin("google")}
          style={{ marginRight: "10px" }}
        >
          Google 로그인
        </button>
        <button onClick={() => handleSocialLogin("kakao")}>
          Kakao 로그인
        </button>
      </div>
    </div>
  );
}

export default LoginForm; // ✅ 외부에서 사용할 수 있도록 컴포넌트 export
