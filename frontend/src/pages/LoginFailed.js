// src/pages/LoginFailed.js

// ✅ React 기본 라이브러리 import
import React from "react";

// ✅ React Router에서 페이지 이동을 도와주는 useNavigate 훅 import
import { useNavigate } from "react-router-dom";

// ✅ 로그인 실패 시 보여줄 컴포넌트 정의
function LoginFailed() {
  // 🔁 페이지 이동을 위한 내비게이션 함수 생성
  const navigate = useNavigate();

  // ✅ 컴포넌트 반환 JSX
  return (
    <div
      style={{
        textAlign: "center",  // 가운데 정렬
        marginTop: "5rem"     // 위쪽 여백
      }}
    >
      {/* ❌ 로그인 실패 메시지 헤더 */}
      <h2>❌ 로그인 실패</h2>

      {/* 🔄 실패 원인 안내 메시지 */}
      <p>소셜 로그인 중 문제가 발생했습니다.</p>

      {/* 🔙 홈으로 이동하는 버튼 (메인 홈으로 리디렉션) */}
      <button onClick={() => navigate("/")}>
        홈으로 이동
      </button>
    </div>
  );
}

// ✅ 외부에서 import 가능하도록 컴포넌트 export
export default LoginFailed;
