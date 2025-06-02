import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import MainHome from './pages/MainHome';
import LoginFailed from './pages/LoginFailed';
import RegisterForm from './components/RegisterForm';
import PromotionListPage from './pages/promotion/PromotionListPage';
import PromotionCreatePage from './pages/promotion/PromotionCreatePage';
import PromotionEditPage from './pages/promotion/PromotionEditPage';
import PromotionPage from './pages/promotion/PromotionPage';
import { UserProvider } from './components/UserContext';

function App() {
  return (
    // ✅ BrowserRouter: HTML5 히스토리 API 기반의 라우터 설정
    // - 전체 애플리케이션을 라우팅 기능으로 감쌈
    <Router>

      {/* ✅ UserProvider: 로그인 사용자 상태(Context)를 앱 전역에 제공 */}
      <UserProvider>

        {/* ✅ Routes: Route 목록을 정의하는 컨테이너 */}
        <Routes>

          {/* ✅ 메인 홈 페이지 */}
          <Route path="/" element={<MainHome />} />

          {/* ❌ 소셜 로그인 실패 시 이동되는 페이지 */}
          <Route path="/login-failed" element={<LoginFailed />} />

          {/* ✅ 회원가입 폼 페이지 */}
          <Route path="/register" element={<RegisterForm />} />

          {/* ✅ 홍보 게시글 목록 페이지 */}
          <Route path="/promotions" element={<PromotionListPage />} />

          {/* ✅ 홍보 게시글 생성 페이지 */}
          <Route path="/promotions/new" element={<PromotionCreatePage />} />

          {/* ✅ 홍보 게시글 수정 페이지 (id는 URL 파라미터) */}
          <Route path="/promotions/edit/:id" element={<PromotionEditPage />} />

          {/* ✅ 홍보 게시글 상세 보기 페이지 (id는 URL 파라미터) */}
          <Route path="/promotions/:id" element={<PromotionPage />} />

        </Routes>
      </UserProvider>
    </Router>
  );
}

export default App;
