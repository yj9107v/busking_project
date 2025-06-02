import React, { useState } from "react"; // React 및 useState 훅 import
import { useNavigate } from "react-router-dom"; // 페이지 이동을 위한 useNavigate 훅 import

function RegisterForm() {
  const navigate = useNavigate(); // 페이지 이동 함수

  // 📦 회원가입 폼 입력값 상태
  const [formData, setFormData] = useState({
    username: "",     // 사용자 ID
    password: "",     // 비밀번호
    email: "",        // 이메일
    nickname: "",     // 닉네임
  });

  // ❌ 에러 메시지 상태
  const [error, setError] = useState("");
  // ✅ 성공 메시지 상태
  const [success, setSuccess] = useState("");

  // 🖊️ 입력 필드 변경 핸들러
  const handleChange = (e) => {
    const { name, value } = e.target;
    // 기존 상태를 복사해서 name 속성에 해당하는 값만 수정
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  // 📨 회원가입 폼 제출 핸들러
  const handleSubmit = async (e) => {
    e.preventDefault(); // 폼 제출 기본 동작 방지
    setError("");       // 에러 초기화
    setSuccess("");     // 성공 메시지 초기화
  
    try {
      const res = await fetch("http://localhost:8080/api/users/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include", // 세션 쿠키 포함
        body: JSON.stringify(formData), // JSON 형식으로 입력값 전송
      });
  
      if (res.ok) {
        // 회원가입 성공 → 메시지 보여주고 홈으로 이동
        setSuccess("✅ 회원가입 성공! 로그인 페이지로 이동합니다.");
        setTimeout(() => navigate("/"), 2000); // 2초 후 홈으로 이동
      } else {
        // 서버에서 반환한 에러 메시지를 파싱
        const err = await res.json();
        setError(`회원가입 실패: ${err.message}`);
      }
    } catch (err) {
      // 네트워크 오류 또는 예외 발생
      setError("서버 오류: " + err.message);
    }
  };

  // ✅ JSX 렌더링
  return (
    <div
      style={{
        maxWidth: "400px",              // 최대 너비 제한
        margin: "3rem auto",            // 가운데 정렬
        padding: "2rem",                // 패딩
        border: "1px solid #ccc",       // 테두리
        borderRadius: "10px",           // 둥근 테두리
      }}
    >
      <h2 style={{ textAlign: "center", marginBottom: "1rem" }}>
        📩 회원가입
      </h2>

      {/* ❌ 에러 메시지 표시 */}
      {error && (
        <div
          style={{
            backgroundColor: "#ffe0e0", padding: "10px",
            marginBottom: "10px", color: "#cc0000"
          }}
        >
          {error}
        </div>
      )}

      {/* ✅ 성공 메시지 표시 */}
      {success && (
        <div
          style={{
            backgroundColor: "#e0ffe0", padding: "10px",
            marginBottom: "10px", color: "#007700"
          }}
        >
          {success}
        </div>
      )}

      {/* 📝 회원가입 입력 폼 */}
      <form
        onSubmit={handleSubmit} // 제출 이벤트 연결
        style={{
          display: "flex",
          flexDirection: "column",
          gap: "12px", // 필드 간 간격
        }}
      >
        <input
          type="text"
          name="username"
          placeholder="아이디"
          value={formData.username}
          onChange={handleChange}
          required
        />
        <input
          type="password"
          name="password"
          placeholder="비밀번호"
          value={formData.password}
          onChange={handleChange}
          required
        />
        <input
          type="email"
          name="email"
          placeholder="이메일"
          value={formData.email}
          onChange={handleChange}
          required
        />
        <input
          type="text"
          name="nickname"
          placeholder="닉네임"
          value={formData.nickname}
          onChange={handleChange}
          required
        />

        {/* 제출 버튼 */}
        <button type="submit">회원가입</button>
      </form>
    </div>
  );
}

// ✅ 외부에서 사용할 수 있도록 컴포넌트 export
export default RegisterForm;
