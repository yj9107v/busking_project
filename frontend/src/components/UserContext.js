// src/context/UserContext.js

import React, { createContext, useContext, useEffect, useState } from "react"; // ✅ React 및 훅들 import

// ✅ Context 객체 생성 (전역 사용자 정보 관리용)
export const UserContext = createContext();

// ✅ 사용자 상태를 전역에서 사용할 수 있도록 하는 Provider 컴포넌트
export function UserProvider({ children }) {
  const [user, setUser] = useState(null); // 👤 로그인된 사용자 정보 상태
  const [isLoading, setIsLoading] = useState(true); // ⏳ 사용자 정보 로딩 상태

  // ✅ 컴포넌트가 처음 마운트될 때 로그인된 사용자 정보 가져오기
  useEffect(() => {
    fetch("http://localhost:8080/api/users/me", {
      credentials: "include", // 🔐 세션 쿠키 포함 (인증 유지)
      headers: { "Accept": "application/json" }
    })
      .then((res) => res.ok ? res.json() : Promise.reject()) // 응답 성공 여부 확인
      .then((data) => {
        console.log("✅ /api/users/me 응답:", data); // 응답 로그 출력
        if (data.username) {
          // ✅ 사용자 정보 저장
          setUser({
            id: data.id, // 🆔 사용자 ID
            username: data.username, // 아이디
            nickname: data.nickname, // 닉네임
            provider: data.provider // 로그인 방식 (local, google, kakao 등)
          });
        }
      })
      .catch(() => setUser(null)) // ❌ 에러 발생 시 user를 null로 설정
      .finally(() => setIsLoading(false)); // ✅ 로딩 상태 false로 변경
  }, []);

  // ✅ 리뷰 작성 시 사용자 정보 디버깅 출력
  console.log("리뷰 작성 시 사용자:", user);

  // ✅ 로그아웃 함수 정의
  const logout = () => {
    fetch("http://localhost:8080/logout", {
      method: "POST", // 서버에 로그아웃 요청
      credentials: "include" // 인증 유지
    })
      .then(() => {
        setUser(null); // 사용자 정보 초기화
        window.location.href = "http://localhost:3000"; // 홈으로 리디렉션
      })
      .catch(err => console.error("Logout failed", err)); // 실패 로그 출력
  };

  // ✅ 전역 Context에 제공할 값 지정
  return (
    <UserContext.Provider value={{ user, setUser, logout, isLoading }}>
      {children} {/* 하위 컴포넌트에 Context 값 제공 */}
    </UserContext.Provider>
  );
}

// ✅ Context 값을 간편하게 가져오는 커스텀 훅
export function useUser() {
  return useContext(UserContext); // 전역 사용자 정보 접근
}
