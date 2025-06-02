// ✅ React 및 훅 import
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

// ✅ 로그인 폼, 지도, 예약 관리자, 네비게이션바 컴포넌트 import
import LoginForm from "../components/LoginForm";
import KakaoMap from "../components/KakaoMap";
import BuskingManager from "../components/buskingManager";
import ReactDOM from "react-dom/client";
import Navbar from "../components/navBar/Navbar";

// ✅ 사용자 상태 Context Provider import
import { UserProvider } from "../components/UserContext";

// ✅ 메인 홈 컴포넌트 정의
function MainHome() {
  // 🔒 로그인된 사용자 정보 상태
  const [user, setUser] = useState(null);

  // 🧾 마이페이지 표시 여부
  const [showMyPage, setShowMyPage] = useState(false);

  // ✏ 닉네임 수정용 입력 필드 상태
  const [nickname, setNickname] = useState("");

  // 📦 라우팅을 위한 useNavigate 훅
  const navigate = useNavigate();

  // ✅ 컴포넌트가 마운트될 때 현재 로그인된 사용자 정보 요청
  useEffect(() => {
    fetch("/api/users/me", { credentials: "include" }) // 쿠키 포함 요청
      .then((res) => (res.ok ? res.json() : Promise.reject()))
      .then((data) => {
        console.log("✅ 로그인 유저 정보:", data);
        if (data.id && data.username) {
          setUser(data);             // 사용자 정보 저장
          setNickname(data.nickname); // 닉네임 상태도 초기화
        }
      })
      .catch(() => setUser(null)); // 실패 시 로그인 상태 아님 처리
  }, []);

  // ✅ 로그아웃 처리 함수
  const handleLogout = () => {
    fetch("/logout", {
      method: "POST",
      credentials: "include",
    }).then(() => {
      setUser(null);      // 사용자 상태 초기화
      navigate("/");      // 홈으로 이동
    });
  };

  // ✅ 회원가입 페이지 이동
  const openRegisterPage = () => {
    navigate("/register");
  };

  // ✅ 예약 등록 팝업 열기 및 BuskingManager 렌더링
  const openReservationPopup = () => {
    const popup = window.open("", "ReservationPopup", "width=600,height=700");
    if (!popup) return alert("팝업 차단을 해제해주세요.");

    // 팝업 HTML 기본 구조 작성
    popup.document.write(`
      <!DOCTYPE html>
      <html lang="ko">
        <head><meta charset="UTF-8"><title>예약 등록</title></head>
        <body><div id="reservation-root"></div></body>
      </html>
    `);
    popup.document.close();

    // React 컴포넌트를 팝업에 마운트 (비동기 타이밍 고려)
    setTimeout(() => {
      const container = popup.document.getElementById("reservation-root");
      if (container) {
        const root = ReactDOM.createRoot(container);
        root.render(
          <UserProvider>
            <BuskingManager />
          </UserProvider>
        );
      }
    }, 100);
  };

  // ✅ 닉네임 수정 처리
  const handleNicknameUpdate = async () => {
    const res = await fetch("/api/users/update-nickname", {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify({ nickname }),
    });

    if (res.ok) {
      const updated = await res.json();
      setUser((prev) => ({ ...prev, nickname: updated.nickname }));
      alert("닉네임 변경 성공");
    } else {
      const err = await res.json();
      alert("변경 실패: " + err.error);
    }
  };

  // ✅ 회원 탈퇴 처리
  const handleWithdraw = async () => {
    if (!window.confirm("정말로 탈퇴하시겠습니까?")) return;

    const res = await fetch("/api/users/withdraw", {
      method: "DELETE",
      credentials: "include",
    });

    if (res.ok) {
      alert("회원 탈퇴 완료");
      setUser(null);
      navigate("/");
    } else {
      const err = await res.json();
      alert("탈퇴 실패: " + err.error);
    }
  };

  // ✅ 컴포넌트 렌더링 시작
  return (
    <div className="MainHome" style={{ padding: "2rem" }}>
      <h2>🎉 버스킹 메인 홈</h2>

      {/* ✅ 로그인 여부에 따라 분기 처리 */}
      {user ? (
        <>
          <p>🔐 로그인 중: <b>{user.username}</b> ({user.provider})</p>
          <p>🙋 닉네임: <b>{user.nickname}</b></p>
          <button onClick={handleLogout}>로그아웃</button>
          <button onClick={() => setShowMyPage(true)}>마이페이지</button>
          <hr />
        </>
      ) : (
        <>
          {/* 🔐 로그인 폼 표시 */}
          <LoginForm
            onLogin={(user) => {
              console.log("📦 로그인된 사용자:", user);
              setUser(user);
              setNickname(user.nickname);
            }}
          />
          <button onClick={openRegisterPage}>회원가입</button>
        </>
      )}

      {/* ✅ 공통 네비게이션 바 */}
      <Navbar />

      {/* ✅ 마이페이지 표시 */}
      {showMyPage && user && (
        <div style={{
          marginTop: "20px",
          padding: "20px",
          border: "1px solid #ccc",
          borderRadius: "8px"
        }}>
          <h3>👤 마이페이지</h3>
          <p>📧 이메일: {user.email}</p>
          <p>👤 아이디: {user.username}</p>

          {/* ✏ 닉네임 수정 입력 */}
          <div style={{ marginTop: "10px" }}>
            <label>
              ✏ 닉네임 변경:
              <input
                type="text"
                value={nickname}
                onChange={(e) => setNickname(e.target.value)}
                style={{ marginLeft: "10px" }}
              />
            </label>
            <button onClick={handleNicknameUpdate}>수정</button>
          </div>

          {/* 📌 예약 등록 버튼 */}
          <div style={{ marginTop: "10px" }}>
            <button onClick={openReservationPopup}>📌 예약 등록</button>
          </div>

          {/* ❌ 회원 탈퇴 & 닫기 */}
          <div style={{ marginTop: "20px" }}>
            <button style={{ color: "red" }} onClick={handleWithdraw}>회원 탈퇴</button>
            <button style={{ marginLeft: "10px" }} onClick={() => setShowMyPage(false)}>닫기</button>
          </div>
        </div>
      )}

      <hr />
      
      {/* ✅ 카카오 맵 표시 */}
      <KakaoMap />
    </div>
  );
}

// ✅ 컴포넌트 외부에서 사용할 수 있도록 export
export default MainHome;
